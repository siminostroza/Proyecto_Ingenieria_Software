package com.logistica.ms_auth.model;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
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
    id_user
    username
    is_activa
    last_login
    
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

    @NotBlank(message = "El username es bbligatorio")
    private String username;

    @NotBlank(message = "El password es obligatorio")
    private String password;

    private Boolean is_active; // Para bloquear o desbloquear

    private Timestamp last_login;

    @PrePersist
    protected void onCreate(){
        this.is_active = true;
        this.last_login = new Timestamp(System.currentTimeMillis());
    }
}
