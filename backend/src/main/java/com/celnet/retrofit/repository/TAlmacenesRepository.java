package com.celnet.retrofit.repository;

import com.celnet.retrofit.model.TAlmacenes;
import com.celnet.retrofit.model.id.TAlmacenesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TAlmacenesRepository extends JpaRepository<TAlmacenes, TAlmacenesId> {
}

