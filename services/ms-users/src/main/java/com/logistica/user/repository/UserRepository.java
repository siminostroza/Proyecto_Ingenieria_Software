package com.logistica.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.logistica.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    public Boolean existsByRut(Integer rut);

    public Boolean existsByCorreo(String correo);

    public Optional<User> findByRut(Integer rut);
}
