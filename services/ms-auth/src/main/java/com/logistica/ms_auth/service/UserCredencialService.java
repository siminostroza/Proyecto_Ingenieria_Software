package com.logistica.ms_auth.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.logistica.ms_auth.client.UserClient;
import com.logistica.ms_auth.dto.UserCredencialRegisterDTO;
import com.logistica.ms_auth.dto.UserCredencialResponseDTO;
import com.logistica.ms_auth.exception.entity.*;
import com.logistica.ms_auth.model.UserCredencial;
import com.logistica.ms_auth.repository.UserCredencialRepository;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserCredencialService {
    private final UserCredencialRepository userCredencialRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaLogProducer logProducer; // Inyección limpia del productor de logs vía Lombok
    private final HttpServletRequest request; // Para capturar el Trace Id
    private final UserClient userClient;

    public void registrarCredenciales(Long id, String username, String password) {
        String traceId = request.getHeader("X-Trace-Id");

        Boolean existeUser = userClient.verificarExistenciaUser(id, null).getBody();
    }

    /*
     * CRUD
     * - LISTAR
     * - ACTUALIZAR
     * - ELIMINAR
     */

    // --- LISTAR --- READ
    @Transactional(readOnly = true)
    public List<UserCredencialResponseDTO> listar() {
        return userCredencialRepository.findAll() // Esto es lo que siempre hemos tenido de toda la vida;
                .stream() // Abre un flujo de datos
                .map(this::convertirAResponseDTO) // Transformamos cada entidad en un DTO
                .toList(); // Agrupa todo en una lista
    }

    // Existe un UserCredencial por id
    public Boolean existeUserCredencialId(Long id) {
        return userCredencialRepository.existsById(id);
    }

    // Existe un UserCredencial por Username
    public Boolean existeUserCredencialUsername(String username) {
        return userCredencialRepository.existsByUsername(username);
    }

    // Encontrar un User Por su ID
    @Transactional(readOnly = true)
    public UserCredencialResponseDTO encontrarUserCredencialId(Long id) {
        return convertirAResponseDTO(userCredencialRepository.findById(id)
                .orElseThrow(() -> {
                    logProducer.sendLog("WARN", "Intento fallido de buscar usuario inexistente con ID: " + id);
                    return new EntityNotFoundException("No se encontró al usuario con la id: " + id);
                }));
    }

    // --- ACTUALIZAR y CREAR ---
    // Comentario Temporal
    // Utilizamos save() del JpaRepository para guardar y actualizar el model
    // Ya no le pedimos que nos retorne un "UserCredencial" ahora es el DTO Response
    // Ya no le pedimos que nos dé un "userCredencial" ahora nos pedirá el DTO
    // Register
    @Transactional
    public UserCredencialResponseDTO crearUserCredencial(UserCredencialRegisterDTO dto) {
        if (userCredencialRepository.existsByUsername(dto.getUsername())) {
            logProducer.sendLog("WARN", "Conflicto al crear usuario. El Username ya existe: " + dto.getUsername());
            throw new EntityConflictException("Ya existe el Username: " + dto.getUsername());
        }

        // Generaremos el Objeto con los datos del dto
        UserCredencial userCredencial = new UserCredencial();
        userCredencial.setUsername(dto.getUsername());
        userCredencial.setPassword(passwordEncoder.encode(dto.getPassword()));

        UserCredencial guardado = userCredencialRepository.save(userCredencial);

        logProducer.sendLog("INFO", "Usuario creado exitosamente con Username: " + guardado.getUsername());

        // Guardamos el DTO en la base de datos
        return convertirAResponseDTO(guardado);
    }

    @Transactional
    public UserCredencialResponseDTO actualizarUserCredencial(Long id, UserCredencialRegisterDTO dto) {
        // Verificamos que el usuario a actualizar exista realmente
        UserCredencial usuarioExistente = userCredencialRepository.findById(id)
                .orElseThrow(() -> {
                    logProducer.sendLog("WARN", "Intento fallido de actualizar usuario inexistente con ID: " + id);
                    return new EntityNotFoundException("No se encontró al usuario con la id: " + id);
                });

        // 2. Validación de Username duplicado por OTRO usuario:
        // Si cambió su username, verificamos que el nuevo no esté tomado por alguien
        // más
        if (!usuarioExistente.getUsername().equals(dto.getUsername()) &&
                userCredencialRepository.existsByUsername(dto.getUsername())) {
            logProducer.sendLog("WARN",
                    "Conflicto al actualizar ID " + id + ". El Username '" + dto.getUsername() + "' ya está ocupado.");
            throw new EntityConflictException(
                    "El Username '" + dto.getUsername() + "' ya está en uso por otro usuario.");
        }

        // 3. Seteamos los cambios seguros
        usuarioExistente.setUsername(dto.getUsername());
        usuarioExistente.setPassword(passwordEncoder.encode(dto.getPassword()));
        if (dto.getIsActive() != null) {
            usuarioExistente.setIsActive(dto.getIsActive());
        }

        logProducer.sendLog("INFO", "Usuario con ID " + id + " actualizado exitosamente.");

        // Al estar utilizando "@Transactional" no necesitamos usar el metodo save del
        // repository
        return convertirAResponseDTO(usuarioExistente);
    }

    // --- Eliminar ---
    @Transactional(readOnly = true)
    public void eliminarUserCredencial(Long id) {
        if (!userCredencialRepository.existsById(id)) {
            logProducer.sendLog("WARN", "Intento fallido de eliminar usuario inexistente con ID: " + id);
            throw new EntityNotFoundException("No se encontró al usuario con la id: " + id);
        }

        userCredencialRepository.deleteById(id);
        logProducer.sendLog("INFO", "Usuario con ID " + id + " fue eliminado del sistema.");
    }

    // ---- codigo muy importante y necesario para poder transformar una objeto
    // UserCredencial a ResponseDTO
    public UserCredencialResponseDTO convertirAResponseDTO(UserCredencial userCredencial) {
        UserCredencialResponseDTO response = new UserCredencialResponseDTO();
        response.setId(userCredencial.getId());
        response.setUsername(userCredencial.getUsername());
        response.setIsActive(userCredencial.getIsActive());
        response.setLastLogin(userCredencial.getLastLogin());
        return response;
    }
}