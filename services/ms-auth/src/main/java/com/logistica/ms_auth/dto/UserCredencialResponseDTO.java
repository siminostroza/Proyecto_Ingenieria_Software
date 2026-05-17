package com.logistica.ms_auth.dto;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class UserCredencialResponseDTO {
    private Long id;
    private String username;
    private Boolean isActive;
    private Timestamp lastLogin;
}