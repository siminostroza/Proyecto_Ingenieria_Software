package com.logistica.ms_auth.model;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
    /*
     * idUser
     * username
     * isActiva
     * lastLogin
     * 
     */

    @Id
    @NotNull(message = "El ID es obligatorio")
    private Long id; // Id asignado por el ms-users

    @Column(unique = true, nullable = false)
    @NotBlank(message = "El username es obligatorio")
    private String username;

    @NotBlank(message = "El password es obligatorio")
    private String password;

    private Boolean isActive; // Para bloquear o desbloquear

    private Timestamp lastLogin;

    @PrePersist
    protected void onCreate() {
        this.isActive = true;
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastLogin = new Timestamp(System.currentTimeMillis());
    }
}