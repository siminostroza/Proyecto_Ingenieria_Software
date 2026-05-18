package com.logistica.ms_security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.logistica.ms_security.model.RoleAssignment;

public interface RoleAssignmentRepository extends JpaRepository<RoleAssignment, Long>{

}
