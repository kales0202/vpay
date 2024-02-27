package com.synway.vpay.repository;

import com.synway.vpay.entity.TempPrice;
import com.synway.vpay.enums.PayType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface TempPriceRepository extends JpaRepository<TempPrice, UUID>, JpaSpecificationExecutor<TempPrice> {

    /**
     * 根据支付方式和支付金额删除订单
     *
     * @param accountId 账户ID
     * @param payType   支付方式
     * @param price     支付金额
     * @return int 删除的数量
     * @since 0.1
     */
    int deleteByAccountIdAndPayTypeAndPrice(UUID accountId, PayType payType, BigDecimal price);

    /**
     * 删除过期的订单金额
     *
     * @param accountId 账户ID
     * @param deadline  截至时间
     * @return 删除的数量
     * @since 0.1
     */
    int deleteByAccountIdAndCreateTimeBefore(UUID accountId, LocalDateTime deadline);
}
