package com.logistica.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.logistica.user.dto.UserRegisterDTO;
import com.logistica.user.dto.UserResponseDTO;
import com.logistica.user.exception.entity.EntityBadRequestException;
import com.logistica.user.exception.entity.EntityConflictException;
import com.logistica.user.exception.entity.EntityNotFoundException;
import com.logistica.user.model.User;
import com.logistica.user.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // Comentario Temporal: RequieredArgsConstructor
public class UserService {
    // Eliminamos la notacion "@Autowired" al utilizar
    // RequiredArgsConstructor de lombok ya no será necesario
    private final UserRepository userRepository;
    private final KafkaLogProducer logProducer;

    // --- Listar ---
    @Transactional(readOnly = true)
    public List<UserResponseDTO> listar() {
        return userRepository.findAll() // Esto es lo que siempre hemos tenido de toda la vida;
                .stream() // Abre un flujo de datos
                .map(this::convertirAResponseDTO) // Transformamos cada entidad en un DTO
                .toList(); // Agrupa todo en una lista
    }

    // Existe un User por id
    public Boolean existeUserId(Long id) {
        return userRepository.existsById(id);
    }

    // Existe un User por su rut
    public Boolean existeUserRut(Integer rut) {
        return userRepository.existsByRut(rut);
    }

    // Encontrar un User Por su ID
    @Transactional(readOnly = true)
    public UserResponseDTO encontrarUserId(Long id) {
        return convertirAResponseDTO(userRepository.findById(id)
                .orElseThrow(() -> {
                    logProducer.sendLog("WARN", "Intento fallido de buscar usuario inexistente con ID: " + id);
                    return new EntityNotFoundException("No se encontró al usuario con la id: " + id);
                }));
    }

    @Transactional(readOnly = true)
    public UserResponseDTO encontrarUserRut(Integer rut) {
        return convertirAResponseDTO(userRepository.findByRut(rut)
                .orElseThrow(() -> {
                    logProducer.sendLog("WARN", "Intento fallido de buscar usuario inexistente con Rut: " + rut);
                    return new EntityNotFoundException("No se encontró al usuario con el rut: " + rut);
                }));
    }

    @Transactional
    public UserResponseDTO crearUser(UserRegisterDTO dto) {
        if (userRepository.existsByRut(dto.getRut())) {
            logProducer.sendLog("WARN", "Conflicto al crear usuario. El Rut ya existe: " + dto.getRut());
            throw new EntityConflictException("Ya existe el Rut: " + dto.getRut());
        }

        // Generaremos el Objeto con los datos del dto
        User guardado = userRepository.save(convertirAEntidad(dto, new User()));
        logProducer.sendLog("INFO", "Usuario creado exitosamente con Rut: " + guardado.getRut());

        // Guardamos el DTO en la base de datos
        return convertirAResponseDTO(guardado);
    }

    @Transactional
    public UserResponseDTO actualizarUser(Long id, UserRegisterDTO dto) {
        // Verificamos que el usuario a actualizar exista realmente
        User usuarioExistente = userRepository.findById(id)
                .orElseThrow(() -> {
                    logProducer.sendLog("WARN", "Intento fallido de actualizar usuario inexistente con ID: " + id);
                    return new EntityNotFoundException("No se encontró al usuario con la id: " + id);
                });

        // 2. Validación de Rut duplicado por OTRO usuario:
        // El usuario no debería poder modificar ni su rut, ni su dv
        if (!dto.getRut().equals(usuarioExistente.getRut())
                || !dto.getDv().equalsIgnoreCase(usuarioExistente.getDv())) {
            throw new EntityBadRequestException("El rut y dv no son campos modificables");
        }

        if (!usuarioExistente.getCorreo().equals(dto.getCorreo()) &&
                userRepository.existsByCorreo(dto.getCorreo())) {
            logProducer.sendLog("WARN",
                    "Conflicto al actualizar ID " + id + ". El Correo '" + dto.getCorreo() + "' ya está ocupado.");
            throw new EntityConflictException(
                    "El Correo '" + dto.getCorreo() + "' ya está en uso por otro usuario.");
        }

        // 3. Seteamos los cambios seguros
        User actualizado = convertirAEntidad(dto, usuarioExistente);
        logProducer.sendLog("INFO", "Usuario con ID " + id + " actualizado exitosamente.");

        // Al estar utilizando "@Transactional" no necesitamos usar el metodo save del
        // repository
        return convertirAResponseDTO(actualizado);
    }

    // --- Eliminar ---
    @Transactional
    public void eliminarUserId(Long id) {
        if (!userRepository.existsById(id)) {
            logProducer.sendLog("WARN", "Intento fallido de eliminar usuario inexistente con ID: " + id);
            throw new EntityNotFoundException("No se encontró al usuario con la id: " + id);
        }

        userRepository.deleteById(id);
        logProducer.sendLog("INFO", "Usuario con ID " + id + " fue eliminado del sistema.");
    }

    @Transactional(readOnly = true)
    public String mensajeTotalUsuarios() {
        Long totalUsuarios = userRepository.count();
        logProducer.sendLog("INFO", "Se mostró el total de usuarios registrados");
        return "Hay " + totalUsuarios + " usuarios registrados";
    }

    // ---------------- METODOS OPENFEIGN ------------------------------

    // ---------------- METODOS UTILITARIOS ---------------------------
    public UserResponseDTO convertirAResponseDTO(User user) {
        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setRut(user.getRut());
        response.setDv(user.getDv());
        response.setPNombre(user.getPNombre());
        response.setSNombre(user.getSNombre());
        response.setApPat(user.getApPat());
        response.setApMat(user.getApMat());
        response.setTelefono(user.getTelefono());
        response.setCorreo(user.getCorreo());
        return response;
    }

    public User convertirAEntidad(UserRegisterDTO dto, User user) {
        user.setRut(dto.getRut());
        user.setDv(dto.getDv());
        user.setPNombre(dto.getPNombre());
        user.setSNombre(dto.getSNombre()); // Puede ser null, pero en creación está bien que se guarde null
        user.setApPat(dto.getApPat());
        user.setApMat(dto.getApMat()); // Puede ser null
        user.setTelefono(dto.getTelefono());
        user.setCorreo(dto.getCorreo());
        return user;
    }
}
