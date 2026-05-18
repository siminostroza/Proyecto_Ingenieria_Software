package com.logistica.ms_quotes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.logistica.ms_quotes.model.Cotizacion;

public interface CotizacionRepository extends JpaRepository<Cotizacion, Long>{

}
