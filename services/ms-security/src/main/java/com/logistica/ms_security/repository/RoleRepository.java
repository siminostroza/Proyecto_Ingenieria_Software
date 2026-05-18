package com.logistica.ms_security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.logistica.ms_security.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
    public Boolean existsById();
    public Boolean existsByRolName();
}
