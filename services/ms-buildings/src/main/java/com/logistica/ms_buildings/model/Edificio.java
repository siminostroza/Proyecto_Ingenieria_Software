package com.logistica.ms_buildings.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ms-edificio")
public class Edificio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "El nombre del edificio es obligatorio")
    @Size(max = 100)
    private String nombre_edificio;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotBlank(message = "La comuna es obligatoria")
    private String comuna;

    @NotBlank(message = "El nombre del administrador es obligatorio")
    private String nombre_administrador;

    @NotBlank(message = "El RUT del administrador es obligatorio")
    @Pattern(regexp = "^[0-9]+-[0-9kK]{1}$", message = "Formato de RUT inválido (ej: 12345678-9)")
    private String rut_administrador;

    private String telefono_conserjeria;

    @Min(value = 1, message = "El edificio debe tener al menos 1 departamento")
    private Integer total_departamentos;

    @DecimalMin(value = "-90.0") @DecimalMax(value = "90.0")
    private Double latitud;

    @DecimalMin(value = "-180.0") @DecimalMax(value = "180.0")
    private Double longitud;
}
