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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "El nombre del rol es obligatorio")
    private String rolName;

    private String jsonPermissions;

    @PrePersist
    public void onCreate() {
        if (this.jsonPermissions == null || this.jsonPermissions.isBlank()) {
            this.jsonPermissions = "[]"; 
        }
    }
}
