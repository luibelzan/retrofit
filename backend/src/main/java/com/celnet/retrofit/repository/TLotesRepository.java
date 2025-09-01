package com.celnet.retrofit.repository;

import com.celnet.retrofit.model.TLotes;
import com.celnet.retrofit.model.id.TLotesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

import java.util.Optional;

@Repository
public interface TLotesRepository extends JpaRepository<TLotes, TLotesId> {

    @Query("SELECT l FROM TLotes l WHERE l.idLote = :idLote AND l.codDistribuidora = :codDistribuidora")
    Optional<TLotes> findByIdLoteAndCodDistribuidora(Integer idLote, String codDistribuidora);

    List<TLotes> findByEstadoLote(String estadoLote);
}
