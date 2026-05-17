package com.logistica.ms_auth.dto;

import lombok.Data;

@Data
public class AuthUserResponseDTO {
    private Long id;
    private String rut;
    private String correo;
}
