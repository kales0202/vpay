package com.synway.vpay.bean;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单统计数据
 *
 * @since 0.1
 */
@Data
public class OrderStatisticsVO {

    /**
     * 今日总订单数量
     *
     * @since 0.1
     */
    private long todayTotal;

    /**
     * 今日成功订单数量
     *
     * @since 0.1
     */
    private long todaySuccess;

    /**
     * 今日失败订单数量
     *
     * @since 0.1
     */
    private long todayFailure;

    /**
     * 总成功订单数量
     *
     * @since 0.1
     */
    private long totalSuccess;

    /**
     * 总失败订单数量
     *
     * @since 0.1
     */
    private long totalFailure;

    /**
     * 今日收入
     *
     * @since 0.1
     */
    private BigDecimal todayIncome;

    /**
     * 总收入
     *
     * @since 0.1
     */
    private BigDecimal totalIncome;
}
