package com.celnet.retrofit.repository;

import com.celnet.retrofit.model.TModelos;
import com.celnet.retrofit.model.id.TModelosId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TModelosRepository extends JpaRepository<TModelos, TModelosId> {
}