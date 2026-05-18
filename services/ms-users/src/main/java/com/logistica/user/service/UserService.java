package com.logistica.user.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.logistica.user.client.AuthClient; 
import com.logistica.user.dto.UserCredencialRegisterDTO; 
import com.logistica.user.dto.UserRegisterDTO;
import com.logistica.user.dto.UserResponseDTO;
import com.logistica.user.exception.entity.EntityBadRequestException;
import com.logistica.user.exception.entity.EntityConflictException;
import com.logistica.user.exception.entity.EntityNotFoundException;
import com.logistica.user.model.User;
import com.logistica.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final KafkaLogProducer logProducer;
    private final AuthClient authClient; 
    private final HttpServletRequest request; 

    @Transactional(readOnly = true)
    public List<UserResponseDTO> listar() {
        return userRepository.findAll()
                .stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    public Boolean existeUserId(Long id) {
        return userRepository.existsById(id);
    }

    // CORREGIDO: Firma adaptada a String
    public Boolean existeUserRut(String rut) {
        return userRepository.existsByRut(rut);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO encontrarUserId(Long id) {
        String traceId = request.getHeader("X-Trace-Id");
        return convertirAResponseDTO(userRepository.findById(id)
                .orElseThrow(() -> {
                    logProducer.sendLog("WARN", "Búsqueda fallida. Usuario ID " + id + " no existe. | TraceId: " + traceId);
                    return new EntityNotFoundException("No se encontró al usuario con la id: " + id);
                }));
    }

    // CORREGIDO: Firma adaptada a String y logs actualizados
    @Transactional(readOnly = true)
    public UserResponseDTO encontrarUserRut(String rut) {
        String traceId = request.getHeader("X-Trace-Id");
        return convertirAResponseDTO(userRepository.findByRut(rut)
                .orElseThrow(() -> {
                    logProducer.sendLog("WARN", "Búsqueda fallida. Usuario RUT " + rut + " no existe. | TraceId: " + traceId);
                    return new EntityNotFoundException("No se encontró al usuario con el rut: " + rut);
                }));
    }

    @Transactional
    public UserResponseDTO crearUser(UserRegisterDTO dto) {
        String traceId = request.getHeader("X-Trace-Id");

        if (userRepository.existsByRut(dto.getRut())) {
            logProducer.sendLog("WARN", "Fallo de registro: El RUT " + dto.getRut() + " ya está registrado. | TraceId: " + traceId);
            throw new EntityConflictException("El RUT ingresado ya pertenece a un usuario en el sistema.");
        }

        if (userRepository.existsByCorreo(dto.getCorreo())) {
            logProducer.sendLog("WARN", "Fallo de registro: El correo " + dto.getCorreo() + " ya está en uso. | TraceId: " + traceId);
            throw new EntityConflictException("El correo electrónico ya se encuentra registrado.");
        }

        User nuevoUser = new User();
        nuevoUser = convertirAEntidad(dto, nuevoUser);
        
        User guardadoLocal = userRepository.saveAndFlush(nuevoUser);
        
        logProducer.sendLog("INFO", "Perfil de usuario guardado localmente con ID: " + guardadoLocal.getId() + " | TraceId: " + traceId);

        try {
            UserCredencialRegisterDTO credencialesDTO = new UserCredencialRegisterDTO();
            credencialesDTO.setId(guardadoLocal.getId());
            credencialesDTO.setUsername(dto.getCorreo()); 
            credencialesDTO.setPassword(dto.getPassword()); 

            authClient.generarCredencialesRemotas(credencialesDTO);
            logProducer.sendLog("INFO", "Credenciales asignadas remotamente en ms-auth para ID: " + guardadoLocal.getId() + " | TraceId: " + traceId);

        } catch (Exception ex) {
            logProducer.sendLog("ERROR", "Error crítico en ms-auth. Abortando registro integral para ID: " + guardadoLocal.getId() + ". Detalle: " + ex.getMessage() + " | TraceId: " + traceId);
            throw new EntityConflictException("No se pudieron registrar las credenciales de seguridad. Proceso de registro cancelado.");
        }

        return convertirAResponseDTO(guardadoLocal);
    }

    @Transactional
    public UserResponseDTO actualizarUser(Long id, UserRegisterDTO dto) {
        String traceId = request.getHeader("X-Trace-Id");

        User usuarioExistente = userRepository.findById(id)
                .orElseThrow(() -> {
                    logProducer.sendLog("WARN", "Intento fallido de actualizar usuario inexistente con ID: " + id + " | TraceId: " + traceId);
                    return new EntityNotFoundException("No se encontró al usuario con la id: " + id);
                });

        // CORREGIDO: Se remueve la evaluación del DV independiente. Al ser el RUT un String completo, 
        // se valida la inmutabilidad de la cadena completa directamente.
        if (!dto.getRut().equals(usuarioExistente.getRut())) {
            throw new EntityBadRequestException("El RUT no es un campo modificable por reglas de auditoría.");
        }

        boolean correoCambiado = !usuarioExistente.getCorreo().equalsIgnoreCase(dto.getCorreo());

        if (correoCambiado && userRepository.existsByCorreo(dto.getCorreo())) {
            logProducer.sendLog("WARN", "Conflicto al actualizar ID " + id + ". El Correo '" + dto.getCorreo() + "' ya está ocupado. | TraceId: " + traceId);
            throw new EntityConflictException("El Correo '" + dto.getCorreo() + "' ya está en uso por otro usuario.");
        }

        User actualizado = convertirAEntidad(dto, usuarioExistente);
        userRepository.saveAndFlush(actualizado);
        logProducer.sendLog("INFO", "Usuario con ID " + id + " actualizado localmente. | TraceId: " + traceId);

        if (correoCambiado) {
            try {
                UserCredencialRegisterDTO credencialesDTO = new UserCredencialRegisterDTO();
                credencialesDTO.setUsername(actualizado.getCorreo());
                credencialesDTO.setPassword(dto.getPassword());

                logProducer.sendLog("INFO", "Propagando cambio de Username a ms-auth para el ID: " + id + " | TraceId: " + traceId);
            } catch (Exception ex) {
                logProducer.sendLog("ERROR", "No se pudo sincronizar el nuevo correo con ms-auth para ID: " + id + " | TraceId: " + traceId);
                throw new EntityConflictException("Error al actualizar las credenciales del sistema. Operación revertida.");
            }
        }

        return convertirAResponseDTO(actualizado);
    }

    @Transactional
    public void eliminarUserId(Long id) {
        String traceId = request.getHeader("X-Trace-Id");
        if (!userRepository.existsById(id)) {
            logProducer.sendLog("WARN", "Intento fallido de eliminar usuario inexistente con ID: " + id + " | TraceId: " + traceId);
            throw new EntityNotFoundException("No se encontró al usuario con la id: " + id);
        }

        userRepository.deleteById(id);
        logProducer.sendLog("INFO", "Usuario con ID " + id + " eliminado del sistema de manera definitiva. | TraceId: " + traceId);
    }

    @Transactional(readOnly = true)
    public String mensajeTotalUsuarios() {
        String traceId = request.getHeader("X-Trace-Id");
        Long totalUsuarios = userRepository.count();
        logProducer.sendLog("INFO", "Consulta de métricas de usuarios ejecutada. | TraceId: " + traceId);
        return "Hay " + totalUsuarios + " usuarios registrados";
    }

    // ---------------- METODOS UTILITARIOS CORREGIDOS ---------------------------
    public UserResponseDTO convertirAResponseDTO(User user) {
        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setRut(user.getRut()); // Asignación directa de String a String
        // ELIMINADO: response.setDv(...) ya no existe.
        response.setPNombre(user.getPNombre());
        response.setSNombre(user.getSNombre());
        response.setApPat(user.getApPat());
        response.setApMat(user.getApMat());
        response.setTelefono(user.getTelefono());
        response.setCorreo(user.getCorreo());
        return response;
    }

    public User convertirAEntidad(UserRegisterDTO dto, User user) {
        user.setRut(dto.getRut()); // Asignación directa de String a String
        // ELIMINADO: user.setDv(...) ya no existe.
        user.setPNombre(dto.getPNombre());
        user.setSNombre(dto.getSNombre());
        user.setApPat(dto.getApPat());
        user.setApMat(dto.getApMat());
        user.setTelefono(dto.getTelefono());
        user.setCorreo(dto.getCorreo());
        return user;
    }
}