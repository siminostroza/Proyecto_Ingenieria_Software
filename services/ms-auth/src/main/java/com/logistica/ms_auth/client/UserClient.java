package com.logistica.ms_auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ms-users", path = "/api/users") // Nombre exacto en Eureka
public interface UserClient {
    @GetMapping("/existe")
    ResponseEntity<Boolean> verificarExistenciaUser(
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "username", required = false) String username);
}