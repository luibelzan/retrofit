package com.celnet.retrofit.service;

import com.celnet.retrofit.model.TFicheros;
import com.celnet.retrofit.model.id.TFicherosId;
import com.celnet.retrofit.repository.TFicherosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TFicherosService {

    private final TFicherosRepository tFicherosRepository;

    @Autowired
    public TFicherosService(TFicherosRepository tFicherosRepository) {
        this.tFicherosRepository = tFicherosRepository;
    }

    public List<TFicheros> findAll() {
        return tFicherosRepository.findAll();
    }

    public Optional<TFicheros> findById(TFicherosId id) {
        return tFicherosRepository.findById(id);
    }

    public TFicheros save(TFicheros tFicheros) {
        return tFicherosRepository.save(tFicheros);
    }

    public void deleteById(TFicherosId id) {
        tFicherosRepository.deleteById(id);
    }
}