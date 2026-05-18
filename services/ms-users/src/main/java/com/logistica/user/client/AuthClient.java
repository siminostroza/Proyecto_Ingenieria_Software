package com.logistica.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// CORRECCIÓN: Se agrega el import faltante para que el compilador reconozca el DTO de respuesta
import com.logistica.user.dto.UserCredencialResponseDTO;
import com.logistica.user.dto.UserCredencialRegisterDTO;

/**
 * CONTRATO DE COMUNICACIÓN SÍNCRONA (OpenFeign)
 * Este archivo VIVE en ms-users, pero apunta de manera lógica
 * al microservicio remoto "ms-auth" registrado en Eureka.
 */
@FeignClient(name = "ms-auth")
public interface AuthClient {

    /**
     * Sincronizado milimétricamente con el @PostMapping raíz 
     * del UserCredencialController de ms-auth (/api/auth).
     */
    @PostMapping("/api/auth") 
    ResponseEntity<UserCredencialResponseDTO> generarCredencialesRemotas(@RequestBody UserCredencialRegisterDTO dto);
}