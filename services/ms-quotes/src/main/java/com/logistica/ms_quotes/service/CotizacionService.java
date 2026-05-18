package com.logistica.ms_quotes.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.logistica.ms_quotes.exception.entity.EntityBadRequestException;
import com.logistica.ms_quotes.exception.entity.EntityConflictException;
import com.logistica.ms_quotes.model.Cotizacion;
import com.logistica.ms_quotes.repository.CotizacionRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CotizacionService {

    private final CotizacionRepository cotizacionRepository;

    // CREAR
    public Cotizacion crearCotizacion(Cotizacion cotizacion) {
        if (cotizacion.getId() != null && cotizacionRepository.existsById(cotizacion.getId())) {
            throw new EntityConflictException("Ya existe una cotización con este ID");
        }
        return cotizacionRepository.save(cotizacion);
    }

    // LEER
    public List<Cotizacion> listarCotizaciones() {
        return cotizacionRepository.findAll();
    }

    // ACTUALIZAR
    @Transactional
    public Cotizacion actualizarCotizacion(Long id, Cotizacion cotizacion) {
        Cotizacion cotizacionExistente = cotizacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se puede actualizar. La cotización con ID " + id + " no existe."));

        if (cotizacion.getId() != null && !cotizacion.getId().equals(id)) {
            throw new EntityBadRequestException("El id ingresado y el de la cotización no coinciden");
        }

        // Actualizar datos
        cotizacionExistente.setUserId(cotizacion.getUserId());
        cotizacionExistente.setBuildingId(cotizacion.getBuildingId());
        cotizacionExistente.setDescription(cotizacion.getDescription());
        cotizacionExistente.setCategory(cotizacion.getCategory());
        cotizacionExistente.setEstimatedAmount(cotizacion.getEstimatedAmount());
        cotizacionExistente.setStatus(cotizacion.getStatus());

        return cotizacionExistente;
    }

    // ELIMINAR
    @Transactional
    public void eliminarCotizacion(Long id) {
        if (!cotizacionRepository.existsById(id)) {
            throw new EntityNotFoundException("No se encontró la cotización a eliminar.");
        }
        cotizacionRepository.deleteById(id);
    }
}