package com.celnet.retrofit.repository;

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
import java.util.List;
import java.util.Optional;

@Repository
public interface TProcesosRepository extends JpaRepository<TProcesos, TProcesosId> {
    Optional<TProcesos> findByIdContador(String idContador);

    List<TProcesos> findByIdLote(Integer idLote);

    // Contar registros por distribuidora y diagnóstico
    @Query("SELECT COUNT(p) FROM TProcesos p WHERE p.codDistribuidora = :codDistribuidora AND p.tipDiagnostico = :tipDiagnostico")
    Long countByCodDistribuidoraAndTipDiagnostico(String codDistribuidora, String tipDiagnostico);

    // Contar registros por distribuidora y diagnóstico con campo codLote nulo
    @Query("SELECT COUNT(p) FROM TProcesos p WHERE p.codDistribuidora = :codDistribuidora AND p.tipDiagnostico = :tipDiagnostico AND p.idLote IS NULL")
    Long countByCodDistribuidoraAndTipDiagnosticoAndCodLoteIsNull(@Param("codDistribuidora") String codDistribuidora, @Param("tipDiagnostico") String tipDiagnostico);

    // Contar todos los registros de una distribuidora
    @Query("SELECT COUNT(p) FROM TProcesos p WHERE p.codDistribuidora = :codDistribuidora")
    Long countByCodDistribuidora(String codDistribuidora);

    // Calcular tiempo medio de procesado en días por distribuidora
    @Query("SELECT p.fecRecepcion, p.fecProceso FROM TProcesos p WHERE p.idContador = :id")
    List<Object[]> getTimestamps(@Param("id") String id);

    // Método para obtener las fechas de proceso y recepción
    @Query(value = "SELECT fec_proceso, fec_recepcion FROM t_proceso WHERE cod_distribuidora = :codDistribuidora AND fec_proceso IS NOT NULL AND fec_recepcion IS NOT NULL", nativeQuery = true)
    List<Object[]> findProcesoDatesByDistribuidora(@Param("codDistribuidora") String codDistribuidora);

    @Modifying
    @Transactional
    @Query("UPDATE TProcesos p SET p.codDiagnostico = :codDiagnostico, p.tipDiagnostico = :tipDiagnostico, p.fecProceso = :fecProceso " +
            "WHERE p.codDistribuidora = :codDistribuidora AND p.idContador = :idContador")
    int updateDiagnostico(@Param("codDistribuidora") String codDistribuidora,
                          @Param("idContador") String idContador,
                          @Param("tipDiagnostico") String tipDiagnostico,
                          @Param("codDiagnostico") Integer codDiagnostico,
                          @Param("fecProceso") LocalDate fecProceso);
}