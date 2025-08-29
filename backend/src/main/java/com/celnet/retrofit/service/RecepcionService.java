package com.celnet.retrofit.service;

import com.celnet.retrofit.model.TGeneral;
import com.celnet.retrofit.model.TProcesos;
import com.celnet.retrofit.model.TAlmacenes;
import com.celnet.retrofit.model.id.TGeneralId;
import com.celnet.retrofit.model.id.TProcesosId;
import com.celnet.retrofit.model.TDistribuidoras;
import com.celnet.retrofit.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.time.Year;


@Service
public class RecepcionService {

    @Autowired
    private TProcesosRepository tProcesosRepository;

    @Autowired
    private TGeneralRepository tGeneralRepository;

    @Autowired
    private RecepcionRepository recepcionRepository;

    @Autowired
    private TDistribuidorasRepository distribuidorasRepository;

    // Este método ahora solo verifica la existencia de un proceso, pero no lo guarda hasta que el usuario confirme
    public String procesarRecepcion(String distribuidora, String tipoEquipo, String almacen, String codigoBarras) throws Exception {
        // Validar longitud y formato del número de serie según tipo de equipo
        validarNumeroSerie(tipoEquipo, codigoBarras, distribuidora);

        String codigoBarrasFixed;
        if(codigoBarras.endsWith("ME") && codigoBarras.length() == 20) {
            codigoBarrasFixed =  codigoBarras.substring(0, codigoBarras.length()-2);
        } else {
            codigoBarrasFixed = codigoBarras;
        }

        // Verificar si el idContador (número de serie) ya existe en t_proceso
        Optional<TProcesos> procesoExistente = tProcesosRepository.findByIdContadorAndCodDistribuidora(codigoBarrasFixed, distribuidora);

        if (procesoExistente.isPresent()) {
            TProcesos proceso = procesoExistente.get();

            // Verificar si CodFabricante, CodModelo o AnoFabricacion están vacíos o nulos
            if(proceso.getCodFabricante() != null && proceso.getCodModelo() != null && proceso.getAnoFabricacion() != null && proceso.getFecRecepcion() != null && proceso.getFecProceso2() != null) {
                if(proceso.getFecRecepcion2() == null) {
                    return "Este contador ya ha sido recepcionado y procesado previamente. Tipo diagnostico: " + proceso.getTipDiagnostico2() + " Codigo diagnostico: " + proceso.getCodDiagnostico2() + " Fecha recepcion: " + proceso.getFecRecepcion();
                } else {
                    return "Este contador ya ha sido recepcionado y procesado previamente. Tipo diagnostico: " + proceso.getTipDiagnostico() + " Codigo diagnostico: " + proceso.getCodDiagnostico() + " Fecha recepcion: " + proceso.getFecRecepcion2();
                }
                
            } else if (proceso.getCodFabricante() != null && proceso.getCodModelo() != null && proceso.getAnoFabricacion() != null) {
                if(proceso.getFecRecepcion2() == null) {
                    // Si hay valores en estos campos, retornamos el mensaje de que el proceso existe
                    return "Este contador ya ha sido recepcionado previamente, revíselo antes de continuar. Fecha de recepcion: " + proceso.getFecRecepcion();
                } else {
                    return "Este contador ya ha sido recepcionado previamente, revíselo antes de continuar. Fecha de recepcion: " + proceso.getFecRecepcion2();
                }
                
            } 
        }

        // Si no existe en t_proceso, verificar en t_general
        boolean existeEnGeneral = tGeneralRepository.existeIdContador(codigoBarrasFixed);
        if (existeEnGeneral) {
            // Si el contador existe en t_general, lo verificamos
            return "Contador veríficado correctamente.";
        }

        // Si no existe el contador ni en t_proceso ni en t_general, se creará una entrada nueva
        return "No existen el elemento escaneado, se creará una entrada nueva.";
    }

    public void guardarRecepcion(String distribuidora, String tipoEquipo, String almacen, String codigoBarras) throws Exception {
        // Verificar existencia en t_general y asignar valores por defecto si no existe
        Optional<TGeneral> cnt = tGeneralRepository.findByIdContadorAndCodDistribuidora(codigoBarras, distribuidora);
        Date fecAveria;
        String desAveria;
        String desObservaciones;
        Date hoy = new Date();
        if(cnt.isPresent()) {
            fecAveria = cnt.get().getFecAveria();
            desAveria = cnt.get().getDesAveria();
            desObservaciones = cnt.get().getDesObservaciones();
        } else {
            fecAveria = java.sql.Date.valueOf("1970-01-01");
            desAveria = "Sin datos.";
            desObservaciones = "NO WEB";
        }

        // Verificar si ya existe un proceso con el idContador
        Optional<TProcesos> procesoExistente = tProcesosRepository.findByIdContadorAndCodDistribuidora(codigoBarras, distribuidora);

        TProcesos proceso;
        if (procesoExistente.isPresent() && "RP".equals(procesoExistente.get().getTipDiagnostico())) {
            // Actualizar registro existente
            proceso = procesoExistente.get();
            proceso.setCodAlmacen(Integer.valueOf(almacen));
            proceso.setFecAveria(fecAveria);
            proceso.setDesAveria(desAveria);
            proceso.setDesObservaciones(desObservaciones);
            proceso.setTipDiagnostico2("RC");
            proceso.setCodDiagnostico2(30);

            // Actualizar los valores de fabricante, modelo, año de fabricación y fecha de recepción
            proceso.setCodFabricante(extraerCodFabricante(codigoBarras));
            proceso.setCodModelo(extraerCodModelo(codigoBarras));
            proceso.setAnoFabricacion(extraerAnoFabricacion(codigoBarras));

            //Actualiza la fecha de recepcion 2
            proceso.setFecRecepcion2(hoy);
        } else if(procesoExistente.isPresent() && !"RP".equals(procesoExistente.get().getTipDiagnostico())) {
            return;
        } else {
            // Crear un nuevo registro
            proceso = new TProcesos();
            proceso.setCodDistribuidora(distribuidora);
            proceso.setTipEquipo(tipoEquipo);
            proceso.setIdContador(codigoBarras); // Número de serie (idContador)
            proceso.setCodFabricante(extraerCodFabricante(codigoBarras));
            proceso.setCodModelo(extraerCodModelo(codigoBarras));
            proceso.setAnoFabricacion(extraerAnoFabricacion(codigoBarras));
            proceso.setFecRecepcion(new Date()); // Fecha actual
            proceso.setCodAlmacen(Integer.valueOf(almacen));
            proceso.setFecAveria(fecAveria);
            proceso.setDesAveria(desAveria);
            proceso.setDesObservaciones(desObservaciones);
            proceso.setTipDiagnostico("RC");
            proceso.setCodDiagnostico(30);
            //proceso.setFecRecepcion2(new Date());
            proceso.setTipDiagnostico2("RC");
            proceso.setCodDiagnostico2(30);
        }

        // Guardar el registro (nuevo o actualizado)
        tProcesosRepository.save(proceso);

    }

    // Método auxiliar para obtener la última fecha disponible para un idContador
    private Date obtenerUltimaFecha(String idContador) {
        return new Date(); // Implementar la lógica para recuperar la última fecha registrada para idContador
    }

    public List<TAlmacenes> obtenerAlmacenes(String distribuidora) {
        return recepcionRepository.findByCodDistribuidora(distribuidora);
    }

    private void validarNumeroSerie(String tipoEquipo, String numeroSerie, String distribuidora) throws Exception {
        //int longitud = tipoEquipo.equals("CN") ? 18 : 20;
        if ((numeroSerie.length() != 18 && numeroSerie.length() != 20) || !numeroSerie.matches("^[A-Z0-9]+$")) {
            throw new Exception("El número de serie debe tener 18 o 20 caracteres alfanuméricos en mayúsculas.");
        }

        Optional<TDistribuidoras> d = distribuidorasRepository.findByCodDistribuidora(distribuidora);
        Integer anioFabricacion = Integer.valueOf(extraerAnoFabricacion(numeroSerie));
        Integer anio = Year.now().getValue();
        Integer diff = anio - anioFabricacion;
        if(d.get().getTipProceso().equals("G") && diff > d.get().getGarantia()) {
            throw new Exception("Equipo fuera de garantia");
        }

        if(d.get().getTipProceso().equals("R") && diff < d.get().getGarantia()) {
            throw new Exception("Equipo en garantia");
        }
        
    }

    private String extraerCodFabricante(String numeroSerie) {
        return numeroSerie.substring(0, 1);
    }

    private String extraerCodModelo(String numeroSerie) {
        return numeroSerie.substring(1, 3);
    }

    private String extraerAnoFabricacion(String numeroSerie) {
        return "20" + numeroSerie.substring(7, 9);
    }

    public void reasignarAlmacen(String codigoBarras, String nuevoAlmacen, String codDistribuidora) throws Exception {
        Optional<TProcesos> procesoOpt = tProcesosRepository.findByIdContadorAndCodDistribuidora(codigoBarras, codDistribuidora);

        if (procesoOpt.isPresent()) {
            TProcesos proceso = procesoOpt.get();
            proceso.setCodAlmacen(Integer.valueOf(nuevoAlmacen)); // Actualizamos el almacén
            tProcesosRepository.save(proceso);
        } else {
            throw new Exception("No se encontró el equipo con el número de serie: " + codigoBarras);
        }
    }

    public List<TAlmacenes> obtenerAlmacenesPorDistribuidora(String codDistribuidora) {
        return recepcionRepository.findByCodDistribuidora(codDistribuidora);
    }
}