package com.logistica.ms_auth.exception.entity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class EntityCreationException extends RuntimeException {
    public EntityCreationException(String mensaje) {
        super(mensaje);
    }
}