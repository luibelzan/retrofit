package com.celnet.retrofit.repository;

import com.celnet.retrofit.model.TDistribuidoras;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface TDistribuidorasRepository extends JpaRepository<TDistribuidoras, String> {

    Optional<TDistribuidoras> findByCodDistribuidora(String codDistribuidora);

    @Query("SELECT d FROM TDistribuidoras d")
    List<TDistribuidoras> findAllDistribuidoras();

    @Query(value = """
        SELECT nom_distribuidora 
        FROM t_distribuidoras 
        WHERE cod_distribuidora = :codDistribuidora
        ORDER BY cod_distribuidora ASC
        """, nativeQuery = true)
    String findNomDistribuidoraByCodDistribuidora(
            @Param("codDistribuidora") String codDistribuidora);
}
