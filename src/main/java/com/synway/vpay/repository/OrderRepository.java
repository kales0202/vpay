package com.synway.vpay.repository;

import com.synway.vpay.entity.Order;
import com.synway.vpay.enums.OrderState;
import com.synway.vpay.enums.PayType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
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
     * 通过订单ID删除订单
     *
     * @param accountId 账户ID
     * @param id        订单ID
     * @return 删除的订单数量
     */
    long deleteByAccountIdAndId(UUID accountId, UUID id);


    /**
     * 批量更新已过期的订单
     *
     * @param accountId 账户ID
     * @param deadline  截至时间
     * @return 过期的订单数量
     */
    @Modifying
    @Query("update pay_order o set o.state = -1 where o.accountId = :accountId and o.state = 0 and o.createTime <= :deadline")
    int updateExpiredOrders(UUID accountId, LocalDateTime deadline);

    /**
     * 统计时间范围内的订单数量
     *
     * @param accountId 账户ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 订单统计数量
     * @since 0.1
     */
    long countByAccountIdAndCreateTimeBetween(UUID accountId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据订单状态统计时间范围内的订单数量
     *
     * @param accountId 账户ID
     * @param state     订单状态
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 统计的订单数量
     * @since 0.1
     */
    long countByAccountIdAndStateAndCreateTimeBetween(UUID accountId, OrderState state, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据订单状态统计订单数量
     *
     * @param accountId 账户ID
     * @param state     订单状态
     * @return 统计的订单数量
     * @since 0.1
     */
    long countByAccountIdAndState(UUID accountId, OrderState state);

    /**
     * 根据支付商户订单号统计订单数量
     *
     * @param accountId 账户ID
     * @param payId     支付商户订单号
     * @return 统计的订单数量
     * @since 0.1
     */
    long countByAccountIdAndPayId(UUID accountId, String payId);

    /**
     * 根据时间范围统计交易成功的订单总额
     *
     * @param accountId 账户ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 交易成功的订单总额
     * @since 0.1
     */
    @Query("select sum(o.price) from pay_order o where o.accountId = :accountId and o.state = 2 and o.createTime between :startTime and :endTime")
    BigDecimal sumPrice(UUID accountId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 统计交易成功的总订单总额
     *
     * @param accountId 账户ID
     * @return 交易成功的总订单总额
     * @since 0.1
     */
    @Query("select sum(o.price) from pay_order o where o.accountId = :accountId and o.state = 2")
    BigDecimal sumPrice(UUID accountId);

    /**
     * 通过订单ID获取订单信息
     *
     * @param accountId 账户ID
     * @param orderId   订单ID
     * @return 订单信息
     * @since 0.1
     */
    Order findByAccountIdAndOrderId(UUID accountId, String orderId);

    /**
     * 通过时间获取订单信息
     *
     * @param accountId 账户ID
     * @param payTime   支付时间
     * @return 订单信息
     * @since 0.1
     */
    Order findByAccountIdAndPayTime(UUID accountId, LocalDateTime payTime);

    /**
     * 通过实际支付金额、订单状态、支付类型获取订单信息
     *
     * @param accountId   账户ID
     * @param reallyPrice 实际支付金额
     * @param state       订单状态
     * @param payType     支付方式
     * @return 订单信息
     * @since 0.1
     */
    Order findByAccountIdAndReallyPriceAndStateAndPayType(UUID accountId, BigDecimal reallyPrice, OrderState state, PayType payType);
}
