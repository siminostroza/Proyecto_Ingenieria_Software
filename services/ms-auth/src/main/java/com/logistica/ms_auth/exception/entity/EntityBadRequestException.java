package com.logistica.ms_auth.exception.entity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EntityBadRequestException extends RuntimeException {
    public EntityBadRequestException(String mensaje) {
        super(mensaje);
    }
}
