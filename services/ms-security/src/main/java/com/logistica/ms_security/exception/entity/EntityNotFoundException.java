package com.logistica.ms_auth.exception.entity;

/**
 * EXCEPCIÓN DE NEGOCIO: RECURSO NO ENCONTRADO (404)
 * OPTIMIZACIÓN: Se remueve la anotación @ResponseStatus para unificar el control
 * y formateo en el GlobalExceptionHandler de ms-auth.
 */
public class EntityNotFoundException extends RuntimeException {
    
    public EntityNotFoundException(String mensaje) {
        super(mensaje);
    }
}