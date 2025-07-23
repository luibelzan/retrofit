package com.celnet.retrofit.service;

import com.celnet.retrofit.model.TProcesos;
import com.celnet.retrofit.model.id.TProcesosId;
import com.celnet.retrofit.repository.TProcesosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TProcesosService {

    @Autowired
    private TProcesosRepository tprocesosRepository;

    public List<TProcesos> findAll() {
        return tprocesosRepository.findAll();
    }

    public Optional<TProcesos> findById(TProcesosId id) {
        return tprocesosRepository.findById(id);
    }

    public TProcesos save(TProcesos procesos) {
        return tprocesosRepository.save(procesos);
    }

    public void deleteById(TProcesosId id) {
        tprocesosRepository.deleteById(id);
    }
}