package com.logistica.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO ESPEJO DE SALIDA (ms-users)
 * Incluye validaciones tempranas y consistencia de atributos para OpenFeign.
 */
@Data
public class UserCredencialRegisterDTO {
    private Long id;

    @NotBlank(message = "El username es obligatorio")
    private String username; 

    @NotBlank(message = "La contraseña es obligatoria")
    private String password; 

    // Incluido para mantener simetría exacta con el receptor remoto
    private Boolean isActive = true; 
}