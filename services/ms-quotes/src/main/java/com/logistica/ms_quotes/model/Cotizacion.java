package com.logistica.ms_quotes.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cotizaciones")
public class Cotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // Read-only en el JSON
    private Long id;

    @NotNull(message = "El ID de usuario es obligatorio")
    @Column(name = "user_id", nullable = false, unique = false)
    private Long userId;

    @NotNull(message = "El ID del edificio es obligatorio")
    @Column(name = "building_id", nullable = false, unique = false)
    private Long buildingId;

    @NotBlank(message = "La descripción es obligatoria")
    @Column(name = "description", nullable = false, unique = false, length = 500)
    private String description;

    @NotNull(message = "La categoría es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, unique = false)
    private Category category;

    @NotNull(message = "El monto estimado es obligatorio")
    @Column(name = "estimated_amount", nullable = false, unique = false)
    private Double estimatedAmount;

    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, unique = false)
    private Status status;

    @Column(name = "created_at", nullable = false, unique = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // Read-only en el JSON
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = Status.PENDING; // Estado por defecto si llega nulo
        }
    }
}