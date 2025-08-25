package com.celnet.retrofit.service;

import com.celnet.retrofit.model.TDiagnostico;
import com.celnet.retrofit.model.TProcesos;
import com.celnet.retrofit.model.id.TProcesosId;
import com.celnet.retrofit.repository.TDiagnosticoRepository;
import com.celnet.retrofit.repository.TProcesosRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DiagnosticoService {

    private final TProcesosRepository procesoRepository;
    private final TDiagnosticoRepository diagnosticoRepository;

    public DiagnosticoService(TProcesosRepository procesoRepository, TDiagnosticoRepository diagnosticoRepository) {
        this.procesoRepository = procesoRepository;
        this.diagnosticoRepository = diagnosticoRepository;
    }

    // Método refactorizado para actualizar el diagnóstico y la fecha de proceso
    @Transactional
    public TProcesos actualizarProceso(String idContador, String codDistribuidora, Integer codDiagnostico, String tipDiagnostico) {
        // Actualizar directamente el diagnóstico y la fecha de proceso sin validar existencia del proceso
        int updatedRows = procesoRepository.updateDiagnostico(codDistribuidora, idContador, tipDiagnostico, codDiagnostico, LocalDate.now());

        if (updatedRows == 0) {
            throw new RuntimeException(
                    "No se pudo actualizar el proceso porque no existe el contador con ID: " + idContador + " y distribuidora: " + codDistribuidora);
        }

        // Recuperar y devolver el proceso actualizado
        return procesoRepository.findByIdContador(idContador).orElseThrow(() -> new RuntimeException("Proceso no encontrado"));
    }

    // Método para validar el contador antes de llamar a actualizarProceso
    public boolean validarContador(String idContador, String codDistribuidora) {
        if (idContador == null || codDistribuidora == null) {
            throw new RuntimeException("El ID del contador y la distribuidora no pueden ser nulos");
        }

        // Eliminar sufijo 'ME' si aplica
        if (idContador.length() == 20 && idContador.endsWith("ME")) {
            idContador = idContador.substring(0, idContador.length() - 2);
        }

        // Validar formato de ID del contador corregido
        if (!idContador.matches("^[A-Z0-9]{18}$")) {
            throw new RuntimeException("El número de serie debe tener exactamente 18 o 20 caracteres alfanuméricos en mayúsculas");
        }

        // Verificar si existe el proceso en la base de datos
        Optional<TProcesos> proceso = procesoRepository.findByIdContador(idContador);
        if (!proceso.isPresent()) {
            throw new RuntimeException("El contador no está recepcionado o no existe en el sistema");
        }

        // Verificar que el proceso corresponde con la distribuidora
        if (!proceso.get().getCodDistribuidora().equals(codDistribuidora)) {
            throw new RuntimeException("La distribuidora no coincide con el contador");
        }

        return true;
    }


    @Transactional
    public List<TProcesos> actualizarProcesosMasivos(List<TProcesosId> ids, Integer codDiagnostico, String tipDiagnostico) {
        List<TProcesos> procesos = new ArrayList<>();
        for (TProcesosId id : ids) {
            // Llamar primero a validarContador antes de actualizar el proceso
            if (validarContador(id.getIdContador(), id.getCodDistribuidora())) {
                // Se asume que el diagnóstico puede ser "CH" o "RP" según el contexto
                //String tipoDiagnostico = (codDiagnostico == 1) ? "CH" : "RP";  // Puedes ajustar esta lógica según tus necesidades
                procesos.add(actualizarProceso(id.getIdContador(), id.getCodDistribuidora(), codDiagnostico, tipDiagnostico));
            } else {
                // Si el contador no es válido, podrías lanzar una excepción o registrar el error
                throw new RuntimeException("Contador o distribuidora no válidos para ID: " + id.getIdContador());
            }
        }
        return procesos;
    }
}