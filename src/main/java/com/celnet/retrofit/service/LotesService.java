package com.celnet.retrofit.service;

import com.celnet.retrofit.model.TLotes;
import com.celnet.retrofit.model.TProcesos;
import com.celnet.retrofit.model.id.TLotesId;
import com.celnet.retrofit.repository.TLotesRepository;
import com.celnet.retrofit.repository.TProcesosRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LotesService {

    private final TLotesRepository tLotesRepository;
    private final TProcesosRepository tProcesosRepository;

    public LotesService(TLotesRepository tLotesRepository, TProcesosRepository tProcesosRepository) {
        this.tLotesRepository = tLotesRepository;
        this.tProcesosRepository = tProcesosRepository;
    }

    // Crear un nuevo lote
    public TLotes createLote(TLotes lote) {
        if (tLotesRepository.existsById(new TLotesId(lote.getCodDistribuidora(), lote.getIdLote()))) {
            throw new IllegalArgumentException("El lote ya existe.");
        }
        return tLotesRepository.save(lote);
    }

    // Leer todos los lotes
    public List<TLotes> getAllLotes() {
        return tLotesRepository.findAll();
    }

    // Leer un lote específico por ID compuesto
    public TLotes getLoteById(String codDistribuidora, Integer idLote) {
        TLotesId loteId = new TLotesId(codDistribuidora, idLote);
        return tLotesRepository.findById(loteId)
                .orElseThrow(() -> new IllegalArgumentException("El lote no existe."));
    }

    // Actualizar un lote existente
    public TLotes updateLote(String codDistribuidora, Integer idLote, TLotes loteDetalles) {
        TLotesId loteId = new TLotesId(codDistribuidora, idLote);
        TLotes lote = tLotesRepository.findById(loteId)
                .orElseThrow(() -> new IllegalArgumentException("El lote no existe."));

        // Actualizar los campos relevantes
        lote.setNomLote(loteDetalles.getNomLote());
        lote.setCodAlmacen(loteDetalles.getCodAlmacen());
        lote.setFecLote(loteDetalles.getFecLote());

        return tLotesRepository.save(lote);
    }

    // Eliminar un lote por ID compuesto
    public void deleteLote(String codDistribuidora, Integer idLote) {
        TLotesId loteId = new TLotesId(codDistribuidora, idLote);
        if (!tLotesRepository.existsById(loteId)) {
            throw new IllegalArgumentException("El lote no existe.");
        }
        tLotesRepository.deleteById(loteId);
    }

    @Transactional
    public String asignarLoteAContadores(Integer idLote, List<String> idContadores, String distribuidora) {
        TLotesId loteId = new TLotesId(distribuidora, idLote);
        Optional<TLotes> loteOpt = tLotesRepository.findById(loteId);
        if (loteOpt.isEmpty()) {
            throw new IllegalArgumentException("El lote con ID " + idLote + " no existe para la distribuidora " + distribuidora);
        }

        for (String idContador : idContadores) {
            validarContadorRecepcionado(idContador);

            Optional<TProcesos> procesoOpt = tProcesosRepository.findByIdContador(idContador);
            if (procesoOpt.isPresent()) {
                TProcesos proceso = procesoOpt.get();
                proceso.setIdLote(idLote);
                tProcesosRepository.save(proceso);
            }
        }

        return "Lote " + idLote + " asignado a " + idContadores.size() + " contadores.";
    }

    public void validarContadorRecepcionado(String idContador) {
        Optional<TProcesos> procesoOpt = tProcesosRepository.findByIdContador(idContador);

        if (procesoOpt.isEmpty()) {
            throw new IllegalArgumentException("El contador con ID " + idContador + " no existe en t_procesos.");
        }

        TProcesos proceso = procesoOpt.get();
        if (proceso.getCodFabricante() == null || proceso.getCodModelo() == null || proceso.getAnoFabricacion() == null) {
            throw new IllegalArgumentException("El contador con ID " + idContador + " no ha sido recepcionado. Verifique antes de asignarlo a un lote.");
        }
    }

    public List<TProcesos> obtenerContadoresPorLote(String codDistribuidora, Integer idLote) {
        TLotesId loteId = new TLotesId(codDistribuidora, idLote);
        TLotes lote = tLotesRepository.findById(loteId)
                .orElseThrow(() -> new IllegalArgumentException("El lote no existe."));

        // Obtener todos los procesos que están asignados al lote
        List<TProcesos> procesos = tProcesosRepository.findByIdLote(idLote);

        if (procesos.isEmpty()) {
            throw new IllegalArgumentException("No hay contadores asignados a este lote.");
        }

        return procesos;
    }
}