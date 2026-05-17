package com.logistica.ms_auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserCredencialRegisterDTO {

    @NotBlank(message = "El username es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    private Boolean isActive;
}