package com.synway.vpay.repository;

import com.synway.vpay.base.repository.BaseRepository;
import com.synway.vpay.entity.PayOrder;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class PayOrderRepository extends BaseRepository<PayOrder, Long> {

    public PayOrderRepository(EntityManager entityManager) {
        super(PayOrder.class, entityManager);
    }
}
