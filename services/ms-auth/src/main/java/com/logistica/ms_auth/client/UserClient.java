package com.logistica.ms_auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.logistica.ms_auth.dto.UserResponseDTO;
import com.logistica.ms_auth.dto.UserRegisterDTO;

@FeignClient(name = "ms-users", path = "/api/users") // Nombre exacto en Eureka
public interface UserClient {
    @GetMapping("/existe")
    ResponseEntity<Boolean> verificarExistenciaUser(
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "username", required = false) String username);

    // Aquí enviaremos el DTO para registrar al usuario en el microservicio de
    // usuarios
    @PostMapping()
    ResponseEntity<UserResponseDTO> registrarUser(@RequestBody UserRegisterDTO userRegisterDTO);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> eliminarUserId(@RequestParam Long id);
}