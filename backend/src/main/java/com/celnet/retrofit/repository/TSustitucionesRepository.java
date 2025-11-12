package com.celnet.retrofit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.celnet.retrofit.model.TSustituciones;
import com.celnet.retrofit.model.id.TSustitucionesId;


@Repository
public interface TSustitucionesRepository extends JpaRepository<TSustituciones, TSustitucionesId> {

    @Query("SELECT p FROM TSustituciones p WHERE p.codDistribuidora = :codDistribuidora")
    List<TSustituciones> getSustituciones(@Param("codDistribuidora") String codDistribuidora);

}
