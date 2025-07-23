package com.celnet.retrofit.repository;

import com.celnet.retrofit.model.TGeneral;
import com.celnet.retrofit.model.id.TGeneralId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TGeneralRepositoryExtra {
    void deleteByCodGeneral(String codDistribuidora, String idContador);
}