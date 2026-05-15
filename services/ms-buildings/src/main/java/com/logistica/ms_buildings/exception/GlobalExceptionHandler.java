package com.logistica.ms_buildings.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.logistica.ms_buildings.exception.edificio.EdificioConflictException;
import com.logistica.ms_buildings.exception.edificio.EdificioNotFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice // Indica que esta clase captura excepciones de todos los controladores
public class GlobalExceptionHandler {

    /*
     * Error en caso de no encontrar la clase User
     * // Definimos ExceptionHandler para atrapar un error de tipo
     */

    /*
     * 
     * |------------------------|
     * | ERRORES CON ORIGEN DE -|
     * | CLASES CREADAS -|
     * |------------------------|
     * 
     */
    // ---- USER NOT FOUND ----
    @ExceptionHandler(EdificioNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFound(EdificioNotFoundException ex) {
        // El 'Map' es la construcción de un body para retornarlo
        Map<String, Object> body = new HashMap<>();
        // Aquí ingresamos manualmente la información que queremos devolver
        // Time Stamp para decir tiempo y hora
        body.put("timestamp", LocalDateTime.now());

        // Mensaje para decir que fue lo que ocurrió
        // Utilizamos el argumento 'ex' para ver el error y obtener el mensaje que
        // queremos retornar
        body.put("message", ex.getMessage());

        // en 'Status' especificamos que tipo de error queremos arrojar
        body.put("status", HttpStatus.NOT_FOUND.value());

        // Por ultimo tenemos que retornar un ResponseEntity con todo lo contenido
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    // ---- USER CONFLICT ----
    @ExceptionHandler(EdificioConflictException.class)
    public ResponseEntity<Object> handleUserConflict(EdificioConflictException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        body.put("status", HttpStatus.CONFLICT.value());

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    /*
     * |---------------|
     * | ERRORES DE |
     * | LIBRERIAS |
     * |---------------|
     */

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> HandleJsonError(HttpMessageNotReadableException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Formato de JSON inválido o tipo de dato incorrecto");
        body.put("details", ex.getMostSpecificCause().getCause());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // Error de un BadRequest cuando una validación está mal hecha
    // Error generico cuando hay un body mal hecho
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());

        // Aquí extraemos solo los mensajes que pusimos en las anotaciones (@NotBlank,
        // @Min, etc.)
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));

        body.put("errors", errores);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // Opcional: Capturar cualquier error genéricos (500) o no definido en el
    // controller
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Ocurrió un error interno en el servidor");
        body.put("error", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
