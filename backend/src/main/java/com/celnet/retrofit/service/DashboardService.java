package com.celnet.retrofit.service;

import com.celnet.retrofit.model.TDistribuidoras;
import com.celnet.retrofit.repository.TDistribuidorasRepository;
import com.celnet.retrofit.repository.TGeneralRepository;
import com.celnet.retrofit.repository.TProcesosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private TProcesosRepository tProcesosRepository;

    @Autowired
    private TGeneralRepository tGeneralRepository;

    @Autowired
    private TDistribuidorasRepository distribuidorasRepository;

    public Long getTotalRecepcionados(String codDistribuidora) {
        return tProcesosRepository.countByCodDistribuidoraAndTipDiagnostico(codDistribuidora, "RC");
    }

    public Long getTotalRecuperados(String codDistribuidora) {
        return tProcesosRepository.countByCodDistribuidoraAndTipDiagnostico(codDistribuidora, "RP");
    }

    public Long getTotalAchatarrados(String codDistribuidora) {
        return tProcesosRepository.countByCodDistribuidoraAndTipDiagnostico(codDistribuidora, "CH");
    }

    public Long getTotalPendientesEnviar(String codDistribuidora) {
        return tProcesosRepository.countByCodDistribuidoraAndTipDiagnosticoAndCodLoteIsNull(codDistribuidora, "RP");
    }

    public Double getTasaRecuperacion(String codDistribuidora) {
        Long totalContadores = tProcesosRepository.countByCodDistribuidora(codDistribuidora);
        Long totalRecuperados = getTotalRecuperados(codDistribuidora);
        return totalContadores > 0 ? (double) totalRecuperados / totalContadores : 0.0;
    }

    public Long getPendientesRecibir(String codDistribuidora) {
        return tGeneralRepository.countPendientes(codDistribuidora);
    }

    // Método para calcular el promedio de días entre fec_proceso y fec_recepcion
    public Double getPromedioDias(String codDistribuidora) {
        List<Object[]> procesos = tProcesosRepository.findProcesoDatesByDistribuidora(codDistribuidora);

        long totalDias = 0;
        long count = 0;

        for (Object[] proceso : procesos) {
            // Obtener las fechas como java.sql.Date
            java.sql.Date sqlFecProceso = (java.sql.Date) proceso[0];
            java.sql.Date sqlFecRecepcion = (java.sql.Date) proceso[1];

            if (sqlFecProceso != null && sqlFecRecepcion != null && !sqlFecProceso.equals(java.sql.Date.valueOf("1970-01-01"))) {
                // Calcular los días de diferencia
                long diferenciaMillis = sqlFecProceso.getTime() - sqlFecRecepcion.getTime();
                long dias = diferenciaMillis / (1000 * 60 * 60 * 24); // Convertir de milisegundos a días
                totalDias += dias;
                count++;
            }
        }

        return count > 0 ? (double) totalDias / count : 0.0;
    }

    public List<Map<String, Object>> getAllDistribuidorasWithKpis() {
        List<TDistribuidoras> distribuidoras = distribuidorasRepository.findAllDistribuidoras();

        return distribuidoras.stream()
                .map(distribuidora -> {
                    String codigo = distribuidora.getCodDistribuidora();
                    String nombre = distribuidora.getNomDistribuidora();

                    // Calcular los KPIs por distribuidora
                    Long totalRecepcionados = getTotalRecepcionados(codigo);
                    Long totalRecuperados = getTotalRecuperados(codigo);
                    Long totalAchatarrados = getTotalAchatarrados(codigo);
                    Long totalPendientesEnviar = getTotalPendientesEnviar(codigo);
                    Double tasaRecuperacion = getTasaRecuperacion(codigo);
                    Long contadoresPendientesRecibir = getPendientesRecibir(codigo); // Ahora es un Long
                    Double promedioDias = getPromedioDias(codigo); // Agregar el promedio de días

                    // Mapear los datos en un formato legible, asegurando que sean de tipo Object
                    return Map.<String, Object>of(
                            "codigo", codigo,
                            "nombre", nombre,
                            "totalRecepcionados", totalRecepcionados,
                            "totalRecuperados", totalRecuperados,
                            "totalAchatarrados", totalAchatarrados,
                            "totalPendientesEnviar", totalPendientesEnviar,
                            "tasaRecuperacion", tasaRecuperacion,
                            "contadoresPendientesRecibir", contadoresPendientesRecibir, // Usamos el Long directamente
                            "promedioDias", promedioDias // Incluir el promedio de días
                    );
                })
                .collect(Collectors.toList());
    }
}