package com.celnet.retrofit.repository;

import com.celnet.retrofit.dto.EquipoGarantiaPendienteSustitucion;
import com.celnet.retrofit.dto.ReporteRecuperado;
import com.celnet.retrofit.dto.ReporteAchatarrado;
import com.celnet.retrofit.model.TProcesos;
import com.celnet.retrofit.model.id.TProcesosId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TProcesosRepository extends JpaRepository<TProcesos, TProcesosId> {
        Optional<TProcesos> findByIdContador(String idContador);

        List<TProcesos> findByIdLote(Integer idLote);

        @Query("SELECT p FROM TProcesos p WHERE p.idLote = :idLote AND p.codDistribuidora = :codDistribuidora")
        List<TProcesos> findByIdLoteAndCodDistribuidora(Integer idLote, String codDistribuidora);

        @Query("SELECT p FROM TProcesos p WHERE p.idContador = :idContador AND p.codDistribuidora = :codDistribuidora")
        Optional<TProcesos> findByIdContadorAndCodDistribuidora(String idContador, String codDistribuidora);

        // Contar registros por distribuidora y diagnóstico
        @Query("SELECT COUNT(p) FROM TProcesos p WHERE p.codDistribuidora = :codDistribuidora AND p.tipDiagnostico2 = :tipDiagnostico")
        Long countByCodDistribuidoraAndTipDiagnostico(String codDistribuidora, String tipDiagnostico);

        // Contar registros por distribuidora y diagnóstico con campo codLote nulo
        @Query("SELECT COUNT(p) FROM TProcesos p WHERE p.codDistribuidora = :codDistribuidora AND p.tipDiagnostico2 = :tipDiagnostico AND p.idLote IS NULL")
        Long countByCodDistribuidoraAndTipDiagnosticoAndCodLoteIsNull(
                        @Param("codDistribuidora") String codDistribuidora,
                        @Param("tipDiagnostico") String tipDiagnostico);

        // Contar todos los registros de una distribuidora
        @Query("SELECT COUNT(p) FROM TProcesos p WHERE p.codDistribuidora = :codDistribuidora")
        Long countByCodDistribuidora(String codDistribuidora);

        // Calcular tiempo medio de procesado en días por distribuidora
        @Query("SELECT p.fecRecepcion2, p.fecProceso2 FROM TProcesos p WHERE p.idContador = :id")
        List<Object[]> getTimestamps(@Param("id") String id);

        // Método para obtener las fechas de proceso y recepción
        @Query(value = "SELECT fec_proceso2, fec_recepcion2 FROM t_proceso WHERE cod_distribuidora = :codDistribuidora AND fec_proceso2 IS NOT NULL AND fec_recepcion2 IS NOT NULL", nativeQuery = true)
        List<Object[]> findProcesoDatesByDistribuidora(@Param("codDistribuidora") String codDistribuidora);

        @Modifying
        @Transactional
        @Query("UPDATE TProcesos p SET p.codDiagnostico = :codDiagnostico, p.tipDiagnostico = :tipDiagnostico, p.fecProceso = :fecProceso, p.fecProceso2 = :fecProceso, p.tipDiagnostico2 = :tipDiagnostico, p.codDiagnostico2 = :codDiagnostico "
                        +
                        "WHERE p.codDistribuidora = :codDistribuidora AND p.idContador = :idContador")
        int updateDiagnostico(@Param("codDistribuidora") String codDistribuidora,
                        @Param("idContador") String idContador,
                        @Param("tipDiagnostico") String tipDiagnostico,
                        @Param("codDiagnostico") Integer codDiagnostico,
                        @Param("fecProceso") LocalDate fecProceso);

        @Modifying
        @Transactional
        @Query("UPDATE TProcesos p SET p.codDiagnostico2 = :codDiagnostico2, p.tipDiagnostico2 = :tipDiagnostico2, p.fecProceso2 = :fecProceso2 "
                        +
                        "WHERE p.codDistribuidora = :codDistribuidora AND p.idContador = :idContador")
        int updateDiagnosticoWithFecRecepcion2(@Param("codDistribuidora") String codDistribuidora,
                        @Param("idContador") String idContador,
                        @Param("tipDiagnostico2") String tipDiagnostico2,
                        @Param("codDiagnostico2") Integer codDiagnostico2,
                        @Param("fecProceso2") LocalDate fecProceso2);

        @Query("SELECT p FROM TProcesos p WHERE p.codDistribuidora = :codDistribuidora AND p.fecRecepcion2 >= :fechaInicio AND p.fecRecepcion2 <= :fechaFin")
        List<TProcesos> getTotalEquipos(
                        @Param("codDistribuidora") String codDistribuidora,
                        @Param("fechaInicio") Date fechaInicio,
                        @Param("fechaFin") Date fechaFin);

        @Query("SELECT p FROM TProcesos p WHERE p.codDistribuidora = :codDistribuidora AND p.fecProceso2 >= :fechaInicio AND p.fecProceso2 <= :fechaFin AND tipDiagnostico2 = 'RP'")
        List<TProcesos> getRecuperados(
                        @Param("codDistribuidora") String codDistribuidora,
                        @Param("fechaInicio") Date fechaInicio,
                        @Param("fechaFin") Date fechaFin);

        @Query("SELECT p FROM TProcesos p WHERE p.codDistribuidora = :codDistribuidora AND p.fecRecepcion2 >= :fechaInicio AND p.fecRecepcion2 <= :fechaFin AND tipDiagnostico2 = 'CH' AND p.idContadorSust IS NOT NULL")
        List<TProcesos> getSustituidos(
                        @Param("codDistribuidora") String codDistribuidora,
                        @Param("fechaInicio") Date fechaInicio,
                        @Param("fechaFin") Date fechaFin);

        @Query("SELECT p FROM TProcesos p WHERE p.codDistribuidora = :codDistribuidora AND p.fecRecepcion2 >= :fechaInicio AND p.fecRecepcion2 <= :fechaFin AND tipDiagnostico2 = 'CH' AND p.codDiagnostico2 = 3")
        List<TProcesos> getDanoFisico(
                        @Param("codDistribuidora") String codDistribuidora,
                        @Param("fechaInicio") Date fechaInicio,
                        @Param("fechaFin") Date fechaFin);

        @Query("SELECT p FROM TProcesos p WHERE p.codDistribuidora = :codDistribuidora AND p.fecRecepcion2 >= :fechaInicio AND p.fecRecepcion2 <= :fechaFin AND tipDiagnostico2 = 'RC' AND p.codDiagnostico2 = 30")
        List<TProcesos> getPendientes(
                        @Param("codDistribuidora") String codDistribuidora,
                        @Param("fechaInicio") Date fechaInicio,
                        @Param("fechaFin") Date fechaFin);

        @Query("SELECT p FROM TProcesos p WHERE p.codDistribuidora = :codDistribuidora AND p.fecRecepcion2 >= :fechaInicio")
        List<TProcesos> getReporteRecepcionados(
                        @Param("codDistribuidora") String codDistribuidora,
                        @Param("fechaInicio") Date fechaInicio);

        @Query(value = """
        SELECT p.id_contador AS idContador,
               p.tip_diagnostico2 AS tipDiagnostico2,
               'S' AS valor1,
               '17' AS valor2,
               'Test Funcionales OK' AS descripcion,
               TO_CHAR(p.fec_proceso2, 'DD-MM-YYYY') AS fechaFormateada
        FROM t_proceso p
        WHERE p.cod_distribuidora = :codDistribuidora
          AND p.tip_diagnostico2 = 'RP'
          AND p.fec_proceso2 >= :fechaInicio
        """, nativeQuery = true)
        List<ReporteRecuperado> getReporteRecuperados(
                        @Param("codDistribuidora") String codDistribuidora,
                        @Param("fechaInicio") Date fechaInicio);

        @Query(value = """
        SELECT t_proceso.id_contador as idContador,
          tip_diagnostico2 as tipDiagnostico2,
          'N' as n,
          cod_diagnostico_ib as codDiagnosticoIb,
          des_diagnostico_ib as desDiagnosticoIb,
          TO_CHAR(fec_proceso2, 'DD-MM-YYYY') AS fecha_formateada
        from t_proceso,t_diagnostico
        where t_proceso.cod_distribuidora = :codDistribuidora and
              t_proceso.tip_diagnostico2 = 'CH' and
              t_proceso.cod_diagnostico2 = t_diagnostico.cod_diagnostico  and
            t_proceso.fec_proceso2 >= :fechaInicio
        """, nativeQuery = true)
        List<ReporteAchatarrado> getReporteAchatarrados(
                        @Param("codDistribuidora") String codDistribuidora,
                        @Param("fechaInicio") Date fechaInicio);

}