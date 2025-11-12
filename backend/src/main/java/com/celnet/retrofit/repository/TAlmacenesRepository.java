package com.celnet.retrofit.repository;

import com.celnet.retrofit.model.TAlmacenes;
import com.celnet.retrofit.model.id.TAlmacenesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TAlmacenesRepository extends JpaRepository<TAlmacenes, TAlmacenesId> {

    @Query("SELECT a FROM TAlmacenes a WHERE a.codAlmacen = :codAlmacen AND a.codDistribuidora = :codDistribuidora")
    Optional<TAlmacenes> findByIdAndCodDistribuidora(Integer codAlmacen, String codDistribuidora);
}

