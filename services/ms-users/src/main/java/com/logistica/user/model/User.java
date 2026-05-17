package com.logistica.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    /*
     * id
     * rut
     * dv
     * nombres
     * telefono
     * correo
     */

    // Restriccion que quizá no se conozcan:
    // Column(valor) <- Aquí le damos "restricciones" a la base de datos
    // Aquí nos aseguraremos que al utilizar la base de datos en crudo no generemos
    // filas con datos imposibles para nuestro service
    // - nullable = false <- Nunca puede quedar vacio el dato
    // - unique = true <- El dato debe ser unico e irrepetible, utilizado para los
    // datos mas sensibles
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Aquí matamos 2 pajaros de 1 tiro
    // Si el usuario hace un Post o Put incluye la ID, no podrá ser modificada y no
    // lanzará error de lectura
    // Además asegura que siempre se genere automaticamente la ID

    // ------ IMPORTANTE ---------
    // ESTE ID DEBE SER AUTOGENERADO POR EL MS-AUTH, ESTE CODIGO DEBE SER MODIFICADO
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull(message = "El RUT es obligatorio")
    @Digits(integer = 9, fraction = 0, message = "El RUT debe tener entre 7 y 8 dígitos")
    @Column(nullable = false, unique = true)
    private Integer rut;

    @NotBlank(message = "El dígito verificador es obligatorio")
    @Size(min = 1, max = 1, message = "El DV debe ser un solo carácter")
    @Column(nullable = false, length = 1) // length <- para explicitar el largo
    private String dv;

    @NotBlank(message = "El primer nombre es obligatorio")
    @Size(max = 50)
    @Column(nullable = false)
    private String pNombre;

    @Size(max = 50)
    @Column(nullable = true)
    private String sNombre; // Puede ser nulo, no lleva @NotBlank

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(max = 50)
    @Column(nullable = false)
    private String apPat;

    @Size(max = 50)
    @Column(nullable = true)
    private String apMat;

    @NotNull(message = "El teléfono no puede estar vacío")
    @Digits(integer = 12, fraction = 0)
    @Column(nullable = false)
    private Integer telefono;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ingresar un formato de correo válido")
    @Column(nullable = false, unique = true)
    private String correo;
}
