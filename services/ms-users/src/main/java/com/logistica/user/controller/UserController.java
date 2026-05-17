package com.logistica.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.logistica.user.dto.UserRegisterDTO;
import com.logistica.user.dto.UserResponseDTO;
import com.logistica.user.exception.entity.*;
import com.logistica.user.model.User;
import com.logistica.user.service.KafkaLogProducer;
import com.logistica.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final KafkaLogProducer logProducer;

    // -- Listar
    @GetMapping()
    public ResponseEntity<List<UserResponseDTO>> listaUsers() {
        List<UserResponseDTO> listado = userService.listar();

        if (listado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.ok(listado);
        }
    }

    // -- Existe el usuario por Id o Rut
    public ResponseEntity<Boolean> existeUser(
            // Asignamos el parametro "required" para que no obligue a usar ambos parametros
            // en la URL
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Integer rut) {
        if (id != null && rut != null) {
            logProducer.sendLog("WARN",
                    "Petición a /existe rechazada: Parámetros ambiguos enviados simultáneamente (id=" + id
                            + ", rut=" + rut + ").");
            throw new EntityBadRequestException(
                    "Debe proporcionar solo un parámetro de búsqueda a la vez ('id' o 'rut').");
        }
        if (id != null) {
            return ResponseEntity.ok(userService.existeUserId(id));
        }
        if (rut != null) {
            return ResponseEntity.ok(userService.existeUserRut(rut));
        }
        logProducer.sendLog("WARN",
                "Petición a /existe rechazada: No se enviaron parámetros de búsqueda.");
        throw new EntityBadRequestException(
                "Se requiere al menos un parámetro de búsqueda válido ('id' o 'rut') para verificar la existencia del usuario");
    }

    @PostMapping()
    public ResponseEntity<UserResponseDTO> crearUser(
            @Valid @RequestBody UserRegisterDTO user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.crearUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> actualizarUser(
            @Valid @RequestBody UserRegisterDTO datosActualizados,
            @RequestParam Long id) {
        return ResponseEntity.ok(userService.actualizarUser(id, datosActualizados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUserId(@PathVariable Long id) {
        userService.eliminarUserId(id);
        return ResponseEntity.ok().build();
    }

    // -- Muestra el total de usuarios
    @GetMapping("/total-usuarios")
    public ResponseEntity<String> mensajeTotalUsuarios() {
        String listado = userService.mensajeTotalUsuarios();
        return ResponseEntity.ok(listado);
    }
}
