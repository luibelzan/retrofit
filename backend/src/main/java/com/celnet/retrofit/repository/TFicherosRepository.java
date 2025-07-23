package com.celnet.retrofit.repository;

import com.celnet.retrofit.model.TFicheros;
import com.celnet.retrofit.model.id.TFicherosId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TFicherosRepository extends JpaRepository<TFicheros, TFicherosId> {
}