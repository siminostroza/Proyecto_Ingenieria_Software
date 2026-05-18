package com.logistica.ms_security.controller;

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

import com.logistica.ms_security.model.Role;
import com.logistica.ms_security.service.RoleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;
    // CRUD
    
    // CREAR
    @PostMapping
    public ResponseEntity<Role> crearRole(
    @Valid @RequestBody Role role) {
        return ResponseEntity.status(HttpStatus.CREATED)
        .body(roleService.crearRole(role));
    }
    
    // LEER
    @GetMapping
    public ResponseEntity<List<Role>> listarRole() {
        List<Role> listado = roleService.listarRole();
        
        return listado.isEmpty() 
        ? ResponseEntity.noContent().build() 
        : ResponseEntity.ok(listado);
    }
    
    // ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<Role> actualizarRole(
    @Valid @RequestBody Role datosActualizados,
    @RequestParam Long id) { 
        return ResponseEntity.ok(roleService.actualizarRole(id, datosActualizados));
    }
        
    
    // ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRole(@RequestParam Long id) {
        roleService.eliminarRole(id);
        return ResponseEntity.noContent().build();
    }
}
