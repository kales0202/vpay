package com.synway.vpay.repository;

import com.synway.vpay.entity.PayCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PayCodeRepository extends JpaRepository<PayCode, UUID>, JpaSpecificationExecutor<PayCode> {

    PayCode findByAccountIdAndIdNotAndPayment(UUID accountId, UUID id, String payment);
}
