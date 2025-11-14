package com.celnet.retrofit.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.celnet.retrofit.dto.AchatarradosPorFallo;
import com.celnet.retrofit.dto.ContadoresEnviadosPorLote;
import com.celnet.retrofit.dto.EquipoGarantiaPendienteSustitucion;
import com.celnet.retrofit.dto.TasaEnvioAlmacen;
import com.celnet.retrofit.model.TProcesos;
import com.celnet.retrofit.repository.EstadisticasRepository;
import com.celnet.retrofit.repository.TProcesosRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EstadisticasService {

    @Autowired
    private EstadisticasRepository estadisticasRepository;

    @Autowired
    private TProcesosRepository procesosRepository;

    public List<EquipoGarantiaPendienteSustitucion> getEquiposGarantia(String codDistribuidora, Date fechaInicio) {
        return estadisticasRepository.getEquiposGarantiaPendientesSustitucion(codDistribuidora, fechaInicio);
    }

    public List<AchatarradosPorFallo> getAchatarradosPorFallos(String codDistribuidora, Date fechaInicio) {
        return estadisticasRepository.findAchatarradosPorFallo(codDistribuidora, fechaInicio);
    }

    public List<ContadoresEnviadosPorLote> getContadoresEnviadosPorLotes(String codDistribuidora, Date fechaInicio) {
        return estadisticasRepository.findEnviadosPorLote(codDistribuidora, fechaInicio);
    }

    public List<TasaEnvioAlmacen> getTasaEnvioAlmacen(String codDistribuidora, Date fechaInicio) {
        return estadisticasRepository.findTasaEnvioAlmacen(codDistribuidora, fechaInicio);
    }

    @Transactional
    public TProcesos actualizarContadorSust(String idContador, String codDistribuidora, String idContadorSust, Integer idLote) {
        // 1. Validar que el contador sustituto sea correcto
        String codigoBarrasFixed;
        if (idContadorSust != null && idContadorSust.endsWith("ME") && idContadorSust.length() == 20) {
            codigoBarrasFixed = idContadorSust.substring(0, idContadorSust.length() - 2);
        } else {
            codigoBarrasFixed = idContadorSust;
        }

        // Aquí podrías añadir validaciones adicionales:
        // - Que no sea nulo o vacío
        // - Que no coincida con el idContador original
        if (codigoBarrasFixed == null || codigoBarrasFixed.isBlank()) {
            throw new IllegalArgumentException("El idContadorSust no es válido.");
        }

        if(codigoBarrasFixed.length() > 20 || codigoBarrasFixed.length() < 18) {
            throw new IllegalArgumentException("El Id Contador debe tener 18 o 20 caracteres");
        }

        if (codigoBarrasFixed.equals(idContador)) {
            throw new IllegalArgumentException("El idContadorSust no puede ser igual al idContador original.");
        }

        // 2. Buscar el proceso original
        Optional<TProcesos> optProceso = procesosRepository.findByIdContadorAndCodDistribuidora(idContador, codDistribuidora);
        if (optProceso.isEmpty()) {
            throw new EntityNotFoundException(
                "No se encontró proceso con idContador=" + idContador + " y codDistribuidora=" + codDistribuidora
            );
        }

        // 3. Actualizar el sustituto
        TProcesos proceso = optProceso.get();
        proceso.setIdContadorSust(codigoBarrasFixed);
        
        if(idLote != null) {
            proceso.setIdLote(idLote);
        }

        return procesosRepository.save(proceso);
    }


    
}
