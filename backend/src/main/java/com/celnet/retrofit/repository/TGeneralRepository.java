package com.celnet.retrofit.repository;

import com.celnet.retrofit.model.TGeneral;
import com.celnet.retrofit.model.id.TGeneralId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TGeneralRepository extends JpaRepository<TGeneral, TGeneralId> {

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM TGeneral r WHERE r.idContador = ?1")
    boolean existeIdContador(String idContador);

    // Método para obtener todos los idContador
    @Query("SELECT r.idContador FROM TGeneral r")
    List<String> findAllIdContadores();

    // Método para obtener la última fecha registrada para un idContador
    @Query("SELECT MAX(r.fecAveria) FROM TGeneral r WHERE r.idContador = :idContador")
    Optional<Date> findUltimaFechaByIdContador(@Param("idContador") String idContador);

    // Obtener todos los contadores pendientes de recibir
    @Query("SELECT COUNT(tg.idContador) FROM TGeneral tg " +
            "WHERE tg.codDistribuidora = :codDistribuidora " +
            "AND tg.idContador NOT IN (SELECT tp.idContador FROM TProcesos tp)")
    Long countPendientes(@Param("codDistribuidora") String codDistribuidora);
}