package com.celnet.retrofit.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.celnet.retrofit.dto.AchatarradosPorFallo;
import com.celnet.retrofit.dto.ContadoresEnviadosPorLote;
import com.celnet.retrofit.dto.EquipoGarantiaPendienteSustitucion;
import com.celnet.retrofit.dto.TasaEnvioAlmacen;
import com.celnet.retrofit.repository.EstadisticasRepository;

@Service
public class EstadisticasService {

    @Autowired
    private EstadisticasRepository estadisticasRepository;

    public List<EquipoGarantiaPendienteSustitucion> getEquiposGarantia(String codDistribuidora, Date fechaInicio) {
        return estadisticasRepository.getEquiposGarantiaPendientesSustitucion(codDistribuidora, fechaInicio);
    }

    public List<AchatarradosPorFallo> getAchatarradosPorFallos(String codDistribuidora, Date fechaInicio) {
        return estadisticasRepository.findAchatarradosPorFallo(codDistribuidora, fechaInicio);
    }

    public List<ContadoresEnviadosPorLote> getContadoresEnviadosPorLotes(String codDistribuidora) {
        return estadisticasRepository.findEnviadosPorLote(codDistribuidora);
    }

    public List<TasaEnvioAlmacen> getTasaEnvioAlmacen(String codDistribuidora) {
        return estadisticasRepository.findTasaEnvioAlmacen(codDistribuidora);
    }
    
}
