package com.celnet.retrofit.repository;

import com.celnet.retrofit.model.TLotes;
import com.celnet.retrofit.model.id.TLotesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TLotesRepository extends JpaRepository<TLotes, TLotesId> {
}
