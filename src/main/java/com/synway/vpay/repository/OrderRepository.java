package com.synway.vpay.repository;

import com.synway.vpay.entity.Order;
import com.synway.vpay.enums.OrderState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 订单Repository
 *
 * @since 0.1
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {

    /**
     * 统计时间范围内的订单数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 订单统计数量
     * @since 0.1
     */
    long countByCreateTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据订单状态统计时间范围内的订单数量
     *
     * @param state     订单状态
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 统计的订单数量
     * @since 0.1
     */
    long countByStateAndCreateTimeBetween(OrderState state, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据订单状态统计订单数量
     *
     * @param state 订单状态
     * @return 统计的订单数量
     * @since 0.1
     */
    long countByState(OrderState state);

    /**
     * 根据支付商户订单号统计订单数量
     *
     * @param payId 支付商户订单号
     * @return 统计的订单数量
     * @since 0.1
     */
    long countByPayId(String payId);

    /**
     * 根据时间范围统计交易成功的订单总额
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 交易成功的订单总额
     * @since 0.1
     */
    @Query("select sum(o.price) from pay_order o where o.state = 2 and o.createTime between ?1 and ?2")
    BigDecimal sumPrice(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 统计交易成功的总订单总额
     *
     * @return 交易成功的总订单总额
     * @since 0.1
     */
    @Query("select sum(o.price) from pay_order o where o.state = 2")
    BigDecimal sumPrice();
}
