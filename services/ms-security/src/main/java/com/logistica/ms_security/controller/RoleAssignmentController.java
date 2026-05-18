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
import org.springframework.web.bind.annotation.RestController;

import com.logistica.ms_security.model.RoleAssignment;
import com.logistica.ms_security.service.RoleAssignmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/role-assignments") // MEJORA: Ruta cambiada para reflejar el recurso correcto
@RequiredArgsConstructor
public class RoleAssignmentController {

    private final RoleAssignmentService roleAssignmentService;

    // CRUD

    // CREAR
    @PostMapping
    public ResponseEntity<RoleAssignment> crearRoleAssignment(
            @Valid @RequestBody RoleAssignment roleAssignment) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roleAssignmentService.crearRoleAssignment(roleAssignment));
    }

    // LEER
    @GetMapping
    public ResponseEntity<List<RoleAssignment>> listarRoleAssignments() {
        List<RoleAssignment> listado = roleAssignmentService.listarRoleAssignments();
        
        return listado.isEmpty() 
                ? ResponseEntity.noContent().build() 
                : ResponseEntity.ok(listado);
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<RoleAssignment> actualizarRoleAssignment(
            @Valid @RequestBody RoleAssignment datosActualizados,
            @PathVariable Long id) { 
        return ResponseEntity.ok(roleAssignmentService.actualizarRoleAssignment(id, datosActualizados));
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRoleAssignment(@PathVariable Long id) {
        roleAssignmentService.eliminarRoleAssignment(id);
        return ResponseEntity.noContent().build();
    }
}