package com.logistica.ms_auth.model;

import java.security.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "UserCredencial")
public class UserCredencial {
    @NotNull()
    private Long id; //Identico a ms-user
    
    @NotBlank()
    private String username; 
    
    @NotBlank()
    private String password;
    
    @NotNull()
    private Boolean is_active; //Para bloquear o desbloquear
    
    @NotNull()
    private Timestamp last_login;
}
