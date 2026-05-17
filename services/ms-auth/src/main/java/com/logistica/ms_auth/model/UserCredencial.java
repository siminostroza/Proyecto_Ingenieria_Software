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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Aquí matamos 2 pajaros de 1 tiro
    // Si el usuario hace un Post o Put incluye la ID, no podrá ser modificada y no
    // lanzará error de lectura
    // Además asegura que siempre se genere automaticamente la ID

    // ------ IMPORTANTE ---------
    // ESTE ID DEBE SER AUTOGENERADO POR EL MS-AUTH, ESTE CODIGO DEBE SER MODIFICADO
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id; // Id autogenerado

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