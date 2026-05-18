package com.logistica.ms_security.service;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.logistica.ms_security.exception.entity.EntityBadRequestException;
import com.logistica.ms_security.exception.entity.EntityNotFoundException;
import com.logistica.ms_security.model.RoleAssignment;
import com.logistica.ms_security.repository.RoleAssignmentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleAssignmentService {
    private final RoleAssignmentRepository roleAssignmentRepository;

    public RoleAssignment crearRoleAssignment(RoleAssignment assignment) {
        // MEJORA 1: Eliminado el check 'existsById' con una lógica invertida defectuosa.
        // MEJORA 2: Asegurar que el ID sea null para que JPA ejecute un INSERT y no un UPDATE accidental.
        assignment.setId(null); 
        return roleAssignmentRepository.save(assignment);
    }

    public List<RoleAssignment> listarRoleAssignments() {
        return roleAssignmentRepository.findAll();
    }

    @Transactional
    public RoleAssignment actualizarRoleAssignment(@NonNull Long id, RoleAssignment assignment) {
        RoleAssignment assignmentExistente = roleAssignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se puede actualizar. La asignación con ID " + id + " no existe."));

        if (assignment.getId() != null && !assignment.getId().equals(id)) {
            throw new EntityBadRequestException("El id ingresado y el de la asignación no coinciden.");
        }
        
        assignmentExistente.setIdRole(assignment.getIdRole());
        assignmentExistente.setIdUser(assignment.getIdUser());

        return assignmentExistente;
    }

    @Transactional
    public void eliminarRoleAssignment(@NonNull Long id) {
        if (!roleAssignmentRepository.existsById(id)) {
            throw new EntityNotFoundException("No se encontró la asignación a eliminar.");
        }
        roleAssignmentRepository.deleteById(id);
    }
}