package com.logistica.ms_security.model;

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
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "El nombre del rol es obligatorio")
    private String rolName;

    private String jsonPermissions;

    @PrePersist
    protected void onCreate(){
        if (jsonPermissions.isBlank() || jsonPermissions.isEmpty() || jsonPermissions == null) {
            this.jsonPermissions = new String("[]");
        }
    }
}
