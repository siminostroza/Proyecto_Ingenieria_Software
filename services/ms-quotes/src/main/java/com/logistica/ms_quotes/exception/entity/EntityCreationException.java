package com.logistica.ms_quotes.exception.entity;

/**
 * EXCEPCIÓN DE NEGOCIO: ERROR DE CREACIÓN INTERNO (500)
 * OPTIMIZACIÓN: Se remueve la anotación @ResponseStatus para unificar el control
 * y formateo en el GlobalExceptionHandler de ms-auth.
 */
public class EntityCreationException extends RuntimeException {
    
    public EntityCreationException(String mensaje) {
        super(mensaje);
    }
}