package com.celnet.retrofit.repository;

import com.celnet.retrofit.model.TDiagnostico;
import com.celnet.retrofit.model.id.TDiagnosticoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TDiagnosticoRepository extends JpaRepository<TDiagnostico, TDiagnosticoId> {
    List<TDiagnostico> findByTipDiagnostico(String tipDiagnostico);
}

