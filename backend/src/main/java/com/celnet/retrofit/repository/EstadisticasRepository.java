package com.celnet.retrofit.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.celnet.retrofit.dto.AchatarradosPorFallo;
import com.celnet.retrofit.dto.ContadoresEnviadosPorLote;
import com.celnet.retrofit.dto.EquipoGarantiaPendienteSustitucion;
import com.celnet.retrofit.dto.TasaEnvioAlmacen;
import com.celnet.retrofit.model.TProcesos;
import com.celnet.retrofit.model.id.TProcesosId;

public interface EstadisticasRepository extends JpaRepository<TProcesos, TProcesosId> {

    @Query(value = """
            SELECT t_proceso.cod_distribuidora as codDistribuidora,
                   t_proceso.id_contador as idContador,
                   t_proceso.cod_fabricante as codFabricante,
                   t_proceso.cod_modelo as codModelo,
                   t_proceso.ano_fabricacion as anoFabricacion,
                   t_proceso.fec_recepcion as fecRecepcion,
                   t_proceso.cod_almacen as codAlmacen,
                   t_almacenes.des_almacen as desAlmacen,
                   t_proceso.fec_averia as fecAveria,
                   t_proceso.des_averia as desAveria,
                   t_proceso.des_observaciones as desObservaciones,
                   t_proceso.tip_diagnostico as tipDiagnostico,
                   t_diagnostico.des_diagnostico as desDiagnostico,
                   t_proceso.fec_proceso as fecProceso,
                   t_proceso.cod_diagnostico as codDiagnostico,
                   t_proceso.id_lote as idLote,
                   t_proceso.id_contador_sust as idContadorSust
            FROM public.t_proceso
            JOIN public.t_diagnostico
              ON t_proceso.tip_diagnostico = t_diagnostico.tip_diagnostico
             AND t_proceso.cod_diagnostico = t_diagnostico.cod_diagnostico
            JOIN public.t_almacenes
              ON t_proceso.cod_distribuidora = t_almacenes.cod_distribuidora
             AND t_proceso.cod_almacen = t_almacenes.cod_almacen
            WHERE t_proceso.cod_distribuidora = :codDistribuidora
              AND t_proceso.tip_diagnostico2 = 'CH'
              AND t_proceso.cod_diagnostico2 <> 3
              AND t_proceso.fec_proceso2 >= :fecProceso
              AND t_proceso.id_lote IS NULL
              AND t_proceso.id_contador_sust IS NULL
            ORDER BY t_proceso.cod_modelo, t_proceso.tip_diagnostico, t_proceso.cod_almacen
            """, nativeQuery = true)
    List<EquipoGarantiaPendienteSustitucion> getEquiposGarantiaPendientesSustitucion(
            @Param("codDistribuidora") String codDistribuidora,
            @Param("fecProceso") Date fecProceso);

    @Query(value = """
            SELECT
                tp.cod_diagnostico AS codDiagnostico,
                d.des_diagnostico AS desDiagnostico,
                COUNT(tp.cod_diagnostico) AS cantidad
            FROM t_proceso tp
            JOIN t_diagnostico d
              ON CAST(d.cod_diagnostico AS INTEGER) = tp.cod_diagnostico
            WHERE tp.tip_diagnostico2 = 'CH'
            AND tp.cod_distribuidora = :codDistribuidora
            AND tp.fec_proceso2 >= :fecProceso
            GROUP BY tp.cod_diagnostico, d.des_diagnostico
            ORDER BY tp.cod_diagnostico
            """, nativeQuery = true)
    List<AchatarradosPorFallo> findAchatarradosPorFallo(@Param("codDistribuidora") String codDistribuidora,
            @Param("fecProceso") Date fecProceso);

    @Query(value = """
            SELECT
                tl.id_lote AS idLote,
                tl.nom_lote AS nomLote,
                tl.cod_almacen AS codAlmacen,
                a.des_almacen AS desAlmacen,
                COUNT(tp.id_lote) AS cantidad,
                tl.fec_lote as fecLote
            FROM t_lotes tl
            JOIN t_proceso tp
              ON tl.cod_distribuidora = tp.cod_distribuidora
             AND tl.id_lote = tp.id_lote
            JOIN t_almacenes a
              ON tl.cod_distribuidora = a.cod_distribuidora
             AND tl.cod_almacen = a.cod_almacen
            WHERE tl.cod_distribuidora = :codDistribuidora
            AND tl.fec_lote >= :fecProceso
            GROUP BY tl.id_lote, tl.nom_lote, tl.cod_almacen, a.des_almacen, tl.fec_lote
            ORDER BY tl.id_lote, a.des_almacen
            """, nativeQuery = true)
    List<ContadoresEnviadosPorLote> findEnviadosPorLote(@Param("codDistribuidora") String codDistribuidora, @Param("fecProceso") Date fecProceso);

    @Query(value = """
        SELECT 
            almacenes.cod_almacen AS codigoAlmacen,
            almacenes.des_almacen AS nombreAlmacen,
            COALESCE(recibidos.total_recibidos, 0) AS contadoresRecibidos,
            COALESCE(enviados.total_enviados, 0) AS contadoresEnviados,
            CASE WHEN COALESCE(recibidos.total_recibidos, 0) = 0 THEN 0
                 ELSE (COALESCE(enviados.total_enviados, 0) * 100.0) / COALESCE(recibidos.total_recibidos, 0)
            END AS tasa
        FROM t_almacenes almacenes
        LEFT JOIN LATERAL (
            SELECT tp.cod_almacen, COUNT(tp.id_contador) AS total_recibidos
            FROM t_proceso tp
            WHERE tp.cod_distribuidora = :codDistribuidora
              AND tp.cod_almacen = almacenes.cod_almacen
            GROUP BY tp.cod_almacen
        ) AS recibidos ON TRUE
        LEFT JOIN LATERAL (
            SELECT tl.cod_almacen, COUNT(DISTINCT tp_lote.id_contador) AS total_enviados
            FROM t_lotes tl
            JOIN t_proceso tp_lote 
              ON tl.cod_distribuidora = tp_lote.cod_distribuidora
             AND tl.id_lote = tp_lote.id_lote
            WHERE tl.cod_distribuidora = :codDistribuidora
              AND tl.cod_almacen = almacenes.cod_almacen
              AND tl.fec_lote >= :fecProceso
            GROUP BY tl.cod_almacen
        ) AS enviados ON TRUE
        WHERE almacenes.cod_distribuidora = :codDistribuidora
        ORDER BY tasa, nombreAlmacen
        """, nativeQuery = true)
    List<TasaEnvioAlmacen> findTasaEnvioAlmacen(@Param("codDistribuidora") String codDistribuidora, @Param("fecProceso") Date fecProceso);

}
