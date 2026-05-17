package com.logistica.ms_auth.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.logistica.ms_auth.dto.UserCredencialRegisterDTO;
import com.logistica.ms_auth.dto.UserCredencialResponseDTO;
import com.logistica.ms_auth.exception.entity.EntityBadRequestException;
import com.logistica.ms_auth.service.UserCredencialService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserCredencialController {
    private final UserCredencialService userCredencialService;

    // --- Listar ---
    @GetMapping()
    public ResponseEntity<List<UserCredencialResponseDTO>> listar() {
        List<UserCredencialResponseDTO> listado = userCredencialService.listar();

        if (listado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.ok(listado);
        }
    }

    // -- Existe User by Id
    @GetMapping("/existe/{id}")
    // Cambiamos de PathVariable a RequestParam, esto facilitará la conexion con
    // OpenFeign
    public ResponseEntity<Boolean> existeUser(
            // Asignamos el parametro "required" para que no obligue a usar ambos parametros
            // en la URL
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String username) {
        if (id != null) {
            return ResponseEntity.ok(userCredencialService.existeUserCredencialId(id));
        }
        if (username != null) {
            return ResponseEntity.ok(userCredencialService.existeUserCredencialUsername(username));
        }

        throw new EntityBadRequestException("Debes enviar un id o un username");
    }

    @PostMapping()
    public ResponseEntity<UserCredencialResponseDTO> crearUser(
            @Valid @RequestBody UserCredencialRegisterDTO userCredencial) {
        // Arreglamos esto de acá
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userCredencialService.crearUserCredencial(userCredencial));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserCredencialResponseDTO> actualizarUser(
            @Valid @RequestBody UserCredencialRegisterDTO datosActualizados,
            @RequestParam Long id) {
        return ResponseEntity.ok(userCredencialService.actualizarUserCredencial(id, datosActualizados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUserCredencial(@RequestParam Long id) {
        userCredencialService.eliminarUserCredencial(id);
        return ResponseEntity.noContent().build();
    }
}
