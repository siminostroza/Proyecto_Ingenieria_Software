package com.logistica.ms_buildings.exception.edificio;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EdificioConflictException extends RuntimeException {
    public EdificioConflictException(String mensaje) {
        super(mensaje);
    }
}