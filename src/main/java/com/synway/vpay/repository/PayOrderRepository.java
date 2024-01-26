package com.synway.vpay.repository;

import com.synway.vpay.base.repository.BaseRepository;
import com.synway.vpay.entity.PayOrder;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class PayOrderRepository extends BaseRepository<PayOrder, UUID> {

    public PayOrderRepository(EntityManager entityManager) {
        super(PayOrder.class, entityManager);
    }
}
