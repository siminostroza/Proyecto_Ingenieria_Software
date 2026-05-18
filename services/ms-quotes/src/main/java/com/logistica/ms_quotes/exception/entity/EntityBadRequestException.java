package com.logistica.ms_quotes.exception.entity;

/**
 * EXCEPCIÓN DE NEGOCIO: SOLICITUD INCORRECTA (400)
 * OPTIMIZACIÓN: Se remueve la anotación @ResponseStatus para unificar el control
 * de errores en el GlobalExceptionHandler de ms-auth, manteniendo el código limpio y desacoplado.
 */
public class EntityBadRequestException extends RuntimeException {
    
    public EntityBadRequestException(String mensaje) {
        super(mensaje);
    }
}