package com.logistica.ms_auth.exception.entity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EntityConflictException extends RuntimeException {
    public EntityConflictException(String mensaje) {
        super(mensaje);
    }
}