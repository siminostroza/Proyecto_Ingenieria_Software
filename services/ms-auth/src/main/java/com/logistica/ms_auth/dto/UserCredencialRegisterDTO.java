package com.logistica.ms_auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO ESPEJO DE ENTRADA (ms-auth)
 * Recibe y valida de forma simétrica los datos enviados por ms-users.
 */
@Data
public class UserCredencialRegisterDTO {
    private Long id;

    @NotBlank(message = "El username es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    // Se asigna un valor por defecto seguro en caso de recibir un null síncrono
    private Boolean isActive = true;
}