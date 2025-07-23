package com.celnet.retrofit.repository;

import com.celnet.retrofit.model.TAlmacenes;
import com.celnet.retrofit.model.id.TAlmacenesId;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecepcionRepository extends JpaRepository<TAlmacenes, TAlmacenesId> {
    List<TAlmacenes> findByCodDistribuidora(String codDistribuidora);
}