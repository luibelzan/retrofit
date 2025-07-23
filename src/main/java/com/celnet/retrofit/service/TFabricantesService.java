package com.celnet.retrofit.service;

import com.celnet.retrofit.model.TFabricantes;
import com.celnet.retrofit.repository.TFabricantesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TFabricantesService {

    @Autowired
    private TFabricantesRepository tFabricantesRepository;

    public List<TFabricantes> findAll() {
        return tFabricantesRepository.findAll();
    }

    public Optional<TFabricantes> findById(String codFabricante) {
        return tFabricantesRepository.findById(codFabricante);
    }

    public TFabricantes save(TFabricantes fabricante) {
        return tFabricantesRepository.save(fabricante);
    }

    public TFabricantes update(TFabricantes fabricante) {
        if (tFabricantesRepository.existsById(fabricante.getCodFabricante())) {
            return tFabricantesRepository.save(fabricante);
        }
        throw new RuntimeException("El fabricante no existe.");
    }

    public void deleteById(String codFabricante) {
        tFabricantesRepository.deleteById(codFabricante);
    }
}
