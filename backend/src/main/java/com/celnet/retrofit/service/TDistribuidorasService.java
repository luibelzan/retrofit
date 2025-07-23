package com.celnet.retrofit.service;

import com.celnet.retrofit.model.TDistribuidoras;
import com.celnet.retrofit.repository.TDistribuidorasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TDistribuidorasService {

    @Autowired
    private TDistribuidorasRepository tDistribuidorasRepository;

    public List<TDistribuidoras> findAll() {
        return tDistribuidorasRepository.findAll();
    }

    public Optional<TDistribuidoras> findById(String codDistribuidora) {
        return tDistribuidorasRepository.findById(codDistribuidora);
    }

    public TDistribuidoras save(TDistribuidoras distribuidora) {
        return tDistribuidorasRepository.save(distribuidora);
    }

    public TDistribuidoras update(TDistribuidoras distribuidora) {
        if (tDistribuidorasRepository.existsById(distribuidora.getCodDistribuidora())) {
            return tDistribuidorasRepository.save(distribuidora);
        }
        throw new RuntimeException("La distribuidora no existe.");
    }

    public void deleteById(String codDistribuidora) {
        tDistribuidorasRepository.deleteById(codDistribuidora);
    }
}
