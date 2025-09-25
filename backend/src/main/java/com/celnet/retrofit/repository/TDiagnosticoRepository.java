package com.celnet.retrofit.repository;

import com.celnet.retrofit.model.TDiagnostico;
import com.celnet.retrofit.model.id.TDiagnosticoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface TDiagnosticoRepository extends JpaRepository<TDiagnostico, TDiagnosticoId> {
    List<TDiagnostico> findByTipDiagnostico(String tipDiagnostico);

    @Query("SELECT a.desAlmacen FROM TAlmacenes a where a.codAlmacen = :codAlmacen AND a.codDistribuidora = :codDistribuidora")
    String findDesAlmacen(@Param("codAlmacen") Integer codAlmacen,
                            @Param("codDistribuidora") String codDistribuidora);
}

