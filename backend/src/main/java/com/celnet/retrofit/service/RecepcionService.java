package com.celnet.retrofit.service;

import com.celnet.retrofit.model.TGeneral;
import com.celnet.retrofit.model.TProcesos;
import com.celnet.retrofit.model.TAlmacenes;
import com.celnet.retrofit.model.id.TGeneralId;
import com.celnet.retrofit.model.id.TProcesosId;
import com.celnet.retrofit.repository.*;
        import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RecepcionService {

    @Autowired
    private TProcesosRepository tProcesosRepository;

    @Autowired
    private TGeneralRepository tGeneralRepository;

    @Autowired
    private RecepcionRepository recepcionRepository;

    // Este método ahora solo verifica la existencia de un proceso, pero no lo guarda hasta que el usuario confirme
    public String procesarRecepcion(String distribuidora, String tipoEquipo, String almacen, String codigoBarras) throws Exception {
        // Validar longitud y formato del número de serie según tipo de equipo
        validarNumeroSerie(tipoEquipo, codigoBarras);

        String codigoBarrasFixed;
        if(codigoBarras.endsWith("ME") && codigoBarras.length() == 20) {
            codigoBarrasFixed =  codigoBarras.substring(0, codigoBarras.length()-2);
        } else {
            codigoBarrasFixed = codigoBarras;
        }

        // Verificar si el idContador (número de serie) ya existe en t_proceso
        Optional<TProcesos> procesoExistente = tProcesosRepository.findByIdContador(codigoBarrasFixed);

        if (procesoExistente.isPresent()) {
            TProcesos proceso = procesoExistente.get();

            // Verificar si CodFabricante, CodModelo o AnoFabricacion están vacíos o nulos
            if (proceso.getCodFabricante() != null && proceso.getCodModelo() != null && proceso.getAnoFabricacion() != null) {
                // Si hay valores en estos campos, retornamos el mensaje de que el proceso existe
                return "Este contador ya ha sido recepcionado previamente, revíselo antes de continuar.";
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
        Optional<TGeneral> generalData = Optional.empty();
        if (tGeneralRepository.existeIdContador(codigoBarras)) {
            List<String> contadores = tGeneralRepository.findAllIdContadores();
            generalData = contadores.stream()
                    .filter(id -> id.equals(codigoBarras))
                    .map(id -> tGeneralRepository.findById(new TGeneralId(id, obtenerUltimaFecha(id))))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findFirst();
        }

        // Asignar valores del registro de TGeneral si existe, o valores predeterminados si no existe
        Date fecAveria = generalData.map(TGeneral::getFecAveria).orElse(java.sql.Date.valueOf("1970-01-01"));
        String desAveria = generalData.map(TGeneral::getDesAveria).orElse("Sin datos.");
        String desObservaciones = generalData.map(TGeneral::getDesObservaciones).orElse("NO WEB");

        // Verificar si ya existe un proceso con el idContador
        Optional<TProcesos> procesoExistente = tProcesosRepository.findByIdContador(codigoBarras);

        TProcesos proceso;
        if (procesoExistente.isPresent()) {
            // Actualizar registro existente
            proceso = procesoExistente.get();
            proceso.setCodAlmacen(Integer.valueOf(almacen));
            proceso.setFecAveria(fecAveria);
            proceso.setDesAveria(desAveria);
            proceso.setDesObservaciones(desObservaciones);
            proceso.setTipDiagnostico("RC");
            proceso.setCodDiagnostico(33);

            // Actualizar los valores de fabricante, modelo, año de fabricación y fecha de recepción
            proceso.setCodFabricante(extraerCodFabricante(codigoBarras));
            proceso.setCodModelo(extraerCodModelo(codigoBarras));
            proceso.setAnoFabricacion(extraerAnoFabricacion(codigoBarras));
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
            proceso.setCodDiagnostico(33);
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

    private void validarNumeroSerie(String tipoEquipo, String numeroSerie) throws Exception {
        //int longitud = tipoEquipo.equals("CN") ? 18 : 20;
        if ((numeroSerie.length() != 18 && numeroSerie.length() != 20) || !numeroSerie.matches("^[A-Z0-9]+$")) {
            throw new Exception("El número de serie debe tener 18 o 20 caracteres alfanuméricos en mayúsculas.");
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

    public void reasignarAlmacen(String codigoBarras, String nuevoAlmacen) throws Exception {
        Optional<TProcesos> procesoOpt = tProcesosRepository.findByIdContador(codigoBarras);

        if (procesoOpt.isPresent()) {
            TProcesos proceso = procesoOpt.get();
            proceso.setCodAlmacen(Integer.valueOf(nuevoAlmacen)); // Actualizamos el almacén
            tProcesosRepository.save(proceso);
        } else {
            throw new Exception("No se encontró el equipo con el número de serie: " + codigoBarras);
        }
    }
}