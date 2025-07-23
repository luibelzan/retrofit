package com.celnet.retrofit.repository;

import com.celnet.retrofit.model.TGeneral;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class TGeneralRepositoryExtraImpl implements TGeneralRepositoryExtra {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void deleteByCodGeneral(String codDistribuidora, String idContador) {
        String jpql = "DELETE FROM TGeneral t WHERE t.codDistribuidora = :codDistribuidora AND t.idContador = :idContador";
        entityManager.createQuery(jpql)
                .setParameter("codDistribuidora", codDistribuidora)
                .setParameter("idContador", idContador)
                .executeUpdate();
    }
}