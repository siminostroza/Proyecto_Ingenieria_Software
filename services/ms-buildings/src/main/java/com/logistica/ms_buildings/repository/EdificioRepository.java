package com.logistica.ms_buildings.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.logistica.ms_buildings.model.Edificio;

public interface EdificioRepository extends JpaRepository<Edificio, Long>{
    
}
