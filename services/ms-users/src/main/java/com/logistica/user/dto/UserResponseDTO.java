package com.logistica.user.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private Integer rut;
    private String dv;
    private String pNombre;
    private String sNombre;
    private String apPat;
    private String apMat;
    private Integer telefono;
    private String correo;
}
