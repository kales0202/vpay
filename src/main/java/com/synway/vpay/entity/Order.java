package com.synway.vpay.entity;

import com.synway.vpay.base.entity.BaseEntity;
import com.synway.vpay.enums.OrderState;
import com.synway.vpay.enums.PayCodeType;
import com.synway.vpay.enums.PayType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 订单
 *
 * @since 0.1
 */
@Data
@Entity(name = "pay_order")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Order extends BaseEntity {

    /**
     * 账户ID
     *
     * @since 0.1
     */
    private UUID accountId;

    /**
     * 监控端ID
     *
     * @since 0.1
     */
    private UUID monitorId;

    /**
     * 支付云端唯一订单号
     *
     * @since 0.1
     */
    private String orderId;

    /**
     * 支付商户订单号
     *
     * @since 0.1
     */
    @Column(unique = true)
    private String payId;

    /**
     * 支付时间
     *
     * @since 0.1
     */
    private LocalDateTime payTime;

    /**
     * 关闭时间
     *
     * @since 0.1
     */
    private LocalDateTime closeTime;

    /**
     * 订单自定义参数，会原封不动的返回给异步接口和同步接口
     *
     * @since 0.1
     */
    private String param;

    /**
     * 支付方式
     *
     * @since 0.1
     */
    private PayType payType;

    /**
     * 订单价格
     *
     * @since 0.1
     */
    @Column(columnDefinition = "decimal(18,2)")
    private BigDecimal price;

    /**
     * 实际支付价格
     *
     * @since 0.1
     */
    @Column(columnDefinition = "decimal(18,2)")
    private BigDecimal reallyPrice;

    /**
     * 异步通知地址，如果为空则使用系统后台设置的地址
     *
     * @since 0.1
     */
    private String notifyUrl;

    /**
     * 支付完成后同步跳转地址，将会携带参数跳转
     *
     * @since 0.1
     */
    private String returnUrl;

    /**
     * 订单状态
     *
     * @since 0.1
     */
    private OrderState state;

    /**
     * 支付二维码类型
     *
     * @since 0.1
     */
    private PayCodeType isAuto;

    /**
     * 二维码内容
     *
     * @since 0.1
     */
    private String payUrl;
}
