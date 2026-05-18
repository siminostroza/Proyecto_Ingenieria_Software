package com.logistica.ms_buildings.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.logistica.ms_buildings.model.Edificio;
import com.logistica.ms_buildings.service.EdificioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/edificios")
@RequiredArgsConstructor
public class EdificioController {

    private final EdificioService edificioService;
    
    // CREAR
    @PostMapping
    public ResponseEntity<Edificio> crearEdificio(@Valid @RequestBody Edificio edificio) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(edificioService.crearEdificio(edificio));
    }
    
    // LEER
    @GetMapping
    public ResponseEntity<List<Edificio>> listarEdificios() {
        List<Edificio> listado = edificioService.listarEdificios();
        
        return listado.isEmpty() 
                ? ResponseEntity.noContent().build() 
                : ResponseEntity.ok(listado);
    }
    
    // ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<Edificio> actualizarEdificio(
            @Valid @RequestBody Edificio datosActualizados,
            @RequestParam Long id) { 
        return ResponseEntity.ok(edificioService.actualizarEdificio(id, datosActualizados));
    }
        
    // ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEdificio(@RequestParam Long id) {
        edificioService.eliminarEdificio(id);
        return ResponseEntity.noContent().build();
    }
}