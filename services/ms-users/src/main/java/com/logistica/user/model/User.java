package com.logistica.user.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Aquí matamos 2 pajaros de 1 tiro
    // Si el usuario hace un Post o Put incluye la ID, no podrá ser modificada y no
    // lanzará error de lectura
    // Además asegura que siempre se genere automaticamente la ID
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull(message = "El RUT es obligatorio")
    @Digits(integer = 9, fraction = 0, message = "El RUT debe tener entre 7 y 8 dígitos")
    @Column(nullable = false, unique = true)
    private Integer rut;

    @NotBlank(message = "El dígito verificador es obligatorio")
    @Size(min = 1, max = 1, message = "El DV debe ser un solo carácter")
    @Column(nullable = false, length = 1)
    private String dv;

    @NotBlank(message = "El primer nombre es obligatorio")
    @Size(max = 50)
    @Column(nullable = false)
    private String pnombre;

    @Size(max = 50)
    private String snombre; // Puede ser nulo, no lleva @NotBlank

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(max = 50)
    @Column(nullable = false)
    private String appat;

    @NotBlank(message = "El apellido materno es obligatorio")
    @Size(max = 50)
    @Column(nullable = false)
    private String apmat;

    @NotNull(message = "El teléfono no puede estar vacío")
    @Min(value = 100000000, message = "Número de teléfono inválido")
    private Integer telefono;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ingresar un formato de correo válido")
    @Column(nullable = false, unique = true)
    private String correo;

    @PastOrPresent(message = "La fecha de registro no puede ser futura")
    private LocalDate fechaDeRegistro;

    // Método para asignar la fecha automáticamente antes de guardar en la DB
    @PrePersist
    protected void onCreate() {
        this.fechaDeRegistro = LocalDate.now();
    }
}
