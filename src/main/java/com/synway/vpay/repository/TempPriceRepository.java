package com.synway.vpay.repository;

import com.synway.vpay.base.repository.BaseRepository;
import com.synway.vpay.entity.TempPrice;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class TempPriceRepository extends BaseRepository<TempPrice, UUID> {

    public TempPriceRepository(EntityManager entityManager) {
        super(TempPrice.class, entityManager);
    }
}
