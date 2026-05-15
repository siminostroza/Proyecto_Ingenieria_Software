package com.logistica.ms_buildings.exception.edificio;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EdificioNotFoundException extends RuntimeException {
    public EdificioNotFoundException(String mensaje) {
        super(mensaje);
    }
}
