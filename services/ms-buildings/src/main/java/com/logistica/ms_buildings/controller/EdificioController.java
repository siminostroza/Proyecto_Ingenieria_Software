package com.logistica.ms_buildings.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.logistica.ms_buildings.exception.edificio.EdificioConflictException;
import com.logistica.ms_buildings.model.Edificio;
import com.logistica.ms_buildings.service.EdificioService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/edificio")
public class EdificioController {
    @Autowired
    private EdificioService edificioService;

    @GetMapping()
    public ResponseEntity<List<Edificio>> listaEdificios() {
        List<Edificio> listado = edificioService.listaEdificios();

        if (listado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.ok(listado);
        }
    }

    
    @GetMapping("/existe/{id}")
    public ResponseEntity<Boolean> existeEdificio(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(edificioService.existeEdificioId(id));
    }

    
    @PostMapping()
    public ResponseEntity<Edificio> crearUser(@Valid @RequestBody Edificio edificio) {
        if (!edificioService.existeEdificioId(edificio.getId())) {
            return ResponseEntity.status(HttpStatus.CREATED).body(edificioService.guardarEdificio(edificio));
        } else {
            throw new EdificioConflictException("Ya existe un usuario con el rut: " + edificio.getId());
        }
    }

    

}
