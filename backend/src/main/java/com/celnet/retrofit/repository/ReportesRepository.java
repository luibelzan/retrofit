package com.celnet.retrofit.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.celnet.retrofit.dto.ContadoresPorEnviar;
import com.celnet.retrofit.model.TProcesos;
import com.celnet.retrofit.model.id.TProcesosId;

@Repository
public interface ReportesRepository extends JpaRepository<TProcesos, TProcesosId> {
    
    @Query(value = """
        SELECT
            cod_distribuidora,
            cod_almacen,
            des_almacen,
            Contadores_Enviados,
            Contadores_Recepcion,
            Contadores_Recuperados,
            Contadores_Sustituibles_Garantia,
            Contadores_Sin_Garantia,
            (Contadores_Recepcion + Contadores_Recuperados + Contadores_Sustituibles_Garantia) AS Total_Equipos_a_Devolver
        FROM (
            SELECT
                tp.cod_distribuidora,
                ta.cod_almacen,
                ta.des_almacen,
                COUNT(CASE WHEN tp.id_lote IS NULL THEN tp.id_contador END) AS Contadores_Enviados,
                COUNT(CASE WHEN tp.id_lote IS NULL AND tp.tip_diagnostico = 'RC' THEN tp.id_contador END) AS Contadores_Recepcion,
                COUNT(CASE WHEN tp.id_lote IS NULL AND tp.tip_diagnostico = 'RP' THEN tp.id_contador END) AS Contadores_Recuperados,
                COUNT(CASE WHEN tp.id_lote IS NULL AND tp.tip_diagnostico = 'CH' AND tp.cod_diagnostico != 3 THEN tp.id_contador END) AS Contadores_Sustituibles_Garantia,
                COUNT(CASE WHEN tp.id_lote IS NULL AND tp.tip_diagnostico = 'CH' AND tp.cod_diagnostico = 3 THEN tp.id_contador END) AS Contadores_Sin_Garantia
            FROM
                t_proceso tp
            JOIN
                t_almacenes ta ON tp.cod_distribuidora = ta.cod_distribuidora AND tp.cod_almacen = ta.cod_almacen
            WHERE
                tp.fec_recepcion2 >= :fecha AND
                tp.cod_distribuidora = :codDistribuidora
            GROUP BY
                tp.cod_distribuidora,
                ta.cod_almacen,
                ta.des_almacen
        ) AS Subconsulta
        ORDER BY
            cod_almacen
        """, nativeQuery = true)
        List<ContadoresPorEnviar> findContadoresPorEnviar(
                        @Param("codDistribuidora") String codDistribuidora,
                        @Param("fecha") Date fecha);
}
