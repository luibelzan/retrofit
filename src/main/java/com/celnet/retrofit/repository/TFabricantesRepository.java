package com.celnet.retrofit.repository;

import com.celnet.retrofit.model.TFabricantes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TFabricantesRepository extends JpaRepository<TFabricantes, String> {
}
