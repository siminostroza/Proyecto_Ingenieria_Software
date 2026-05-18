package com.logistica.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El RUT es obligatorio")
    @Pattern(regexp = "^[0-9]+-?[0-9kK]?$", message = "El formato del RUT no es válido")
    @Size(max = 12, message = "El RUT no puede exceder los 12 caracteres")
    @Column(nullable = false, unique = true, length = 12)
    private String rut;

    @NotBlank(message = "El primer nombre es obligatorio")
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String pNombre;

    @Size(max = 50)
    @Column(nullable = true, length = 50)
    private String sNombre;

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String apPat;

    @Size(max = 50)
    @Column(nullable = true, length = 50)
    private String apMat;

    @NotBlank(message = "El teléfono no puede estar vacío")
    @Size(max = 20)
    @Column(nullable = false, length = 20)
    private String telefono;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ingresar un formato de correo válido")
    @Column(nullable = false, unique = true)
    private String correo;
}