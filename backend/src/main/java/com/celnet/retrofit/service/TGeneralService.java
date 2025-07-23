package com.celnet.retrofit.service;

import com.celnet.retrofit.model.TGeneral;
import com.celnet.retrofit.model.id.TGeneralId;
import com.celnet.retrofit.repository.TGeneralRepository;
import com.celnet.retrofit.repository.TGeneralRepositoryExtra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TGeneralService {

    private final TGeneralRepository tGeneralRepository;
    private final TGeneralRepositoryExtra tGeneralRepositoryExtra;

    @Autowired
    public TGeneralService(TGeneralRepository tGeneralRepository, TGeneralRepositoryExtra tGeneralRepositoryExtra) {
        this.tGeneralRepository = tGeneralRepository;
        this.tGeneralRepositoryExtra = tGeneralRepositoryExtra;
    }

    public List<TGeneral> findAll() {
        return tGeneralRepository.findAll();
    }

    public Optional<TGeneral> findById(TGeneralId id) {
        return tGeneralRepository.findById(id);
    }

    public TGeneral save(TGeneral tGeneral) {
        return tGeneralRepository.save(tGeneral);
    }

    public void deleteByCodGeneral(String codDistribuidora, String idContador) {
        tGeneralRepositoryExtra.deleteByCodGeneral(codDistribuidora, idContador);
    }
}