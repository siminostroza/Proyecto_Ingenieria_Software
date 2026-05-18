package com.logistica.ms_security.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.logistica.ms_security.exception.entity.EntityBadRequestException;
import com.logistica.ms_security.exception.entity.EntityConflictException;
import com.logistica.ms_security.model.Role;
import com.logistica.ms_security.repository.RoleRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    // CRUD 

    // CREAR
    public Role crearRole(Role role) {
        if (role.getId() != null && roleRepository.existsById(role.getId())) {
            throw new EntityConflictException("Ya existe un rol con este ID");
        }
        return roleRepository.save(role);
    }

    // LEER
    public List<Role> listarRole() {
        return roleRepository.findAll();
    }
    
    // ACTUALIZAR
    @Transactional
    public Role actualizarRole(Long id, Role role) {
        Role roleExistente = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se puede actualizar. El rol con ID " + id + " no existe."));

        if (role.getId() != null && !role.getId().equals(id)) {
            throw new EntityBadRequestException("El id ingresado y el del ROL no coinciden");
        }
                
        //Actualizar datos
        role.setId(id); 
        roleExistente.setRolName(role.getRolName());
        if (role.getJsonPermissions() != null) {
            roleExistente.setJsonPermissions(role.getJsonPermissions());
        }

        return roleExistente;
    }

    // ELIMINAR
    @Transactional
    public void eliminarRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new EntityNotFoundException("No se encontró el role a eliminar.");
        }

        roleRepository.deleteById(id);
    }
}