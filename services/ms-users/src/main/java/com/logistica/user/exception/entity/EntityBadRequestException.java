package com.logistica.user.exception.entity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EntityBadRequestException extends RuntimeException {
    public EntityBadRequestException(String mensaje) {
        super(mensaje);
    }
}