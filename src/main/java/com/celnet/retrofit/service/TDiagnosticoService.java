package com.celnet.retrofit.service;

import com.celnet.retrofit.model.TDiagnostico;
import com.celnet.retrofit.model.id.TDiagnosticoId;
import com.celnet.retrofit.repository.TDiagnosticoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TDiagnosticoService {

    @Autowired
    private TDiagnosticoRepository tDiagnosticoRepository;

    public List<TDiagnostico> findAll() {
        return tDiagnosticoRepository.findAll();
    }

    public Optional<TDiagnostico> findById(TDiagnosticoId id) {
        return tDiagnosticoRepository.findById(id);
    }

    public TDiagnostico save(TDiagnostico diagnostico) {
        return tDiagnosticoRepository.save(diagnostico);
    }

    public TDiagnostico update(TDiagnostico diagnostico) {
        if (tDiagnosticoRepository.existsById(new TDiagnosticoId(diagnostico.getTipDiagnostico(), diagnostico.getCodDiagnostico()))) {
            return tDiagnosticoRepository.save(diagnostico);
        }
        throw new RuntimeException("El diagnóstico no existe.");
    }

    public void deleteById(TDiagnosticoId id) {
        tDiagnosticoRepository.deleteById(id);
    }
}
