package com.logistica.ms_security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.logistica.ms_security.model.RoleAssignement;

public interface RoleAssignementRepository extends JpaRepository<RoleAssignement, Long>{
    public Boolean exexistsById();

}
