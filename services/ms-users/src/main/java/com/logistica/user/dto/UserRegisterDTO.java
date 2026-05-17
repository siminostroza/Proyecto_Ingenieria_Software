package com.logistica.user.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterDTO {
    @NotNull(message = "El RUT es obligatorio")
    @Digits(integer = 9, fraction = 0, message = "El RUT debe tener entre 7 y 8 dígitos")
    private Integer rut;

    @NotBlank(message = "El dígito verificador es obligatorio")
    @Size(min = 1, max = 1, message = "El DV debe ser un solo carácter")
    private String dv;

    @NotBlank(message = "El primer nombre es obligatorio")
    @Size(max = 50)
    private String pNombre;

    @Size(max = 50)
    private String sNombre; // Puede ser nulo, no lleva @NotBlank

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(max = 50)
    private String apPat;

    @Size(max = 50)
    private String apMat;

    @NotNull(message = "El teléfono no puede estar vacío")
    @Digits(integer = 12, fraction = 0)
    private Integer telefono;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ingresar un formato de correo válido")
    private String correo;

}
