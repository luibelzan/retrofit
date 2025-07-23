package com.celnet.retrofit.service;

import com.celnet.retrofit.model.TModelos;
import com.celnet.retrofit.model.id.TModelosId;
import com.celnet.retrofit.repository.TModelosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TModelosService {

    @Autowired
    private TModelosRepository tmodelosRepository;

    public List<TModelos> findAll() {
        return tmodelosRepository.findAll();
    }

    public Optional<TModelos> findById(TModelosId id) {
        return tmodelosRepository.findById(id);
    }

    public TModelos save(TModelos modelo) {
        return tmodelosRepository.save(modelo);
    }

    public TModelos update(TModelos modelo) {
        if (tmodelosRepository.existsById(new TModelosId(modelo.getCodModelo(), modelo.getCodFabricante()))) {
            return tmodelosRepository.save(modelo);
        }
        throw new RuntimeException("El modelo no existe.");
    }

    public void deleteById(TModelosId id) {
        tmodelosRepository.deleteById(id);
    }
}

