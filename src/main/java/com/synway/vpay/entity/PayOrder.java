package com.synway.vpay.entity;

import com.synway.vpay.base.entity.BaseEntity;
import com.synway.vpay.enums.OrderState;
import com.synway.vpay.enums.PayQRCodeType;
import com.synway.vpay.enums.PayType;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 订单
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PayOrder extends BaseEntity {

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
    private PayType type;

    /**
     * 订单价格
     *
     * @since 0.1
     */
    private double price;

    /**
     * 实际支付价格
     *
     * @since 0.1
     */
    private double reallyPrice;

    /**
     * 异步地址
     *
     * @since 0.1
     */
    private String notifyUrl;

    /**
     * 支付完成后跳转地址
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
    private PayQRCodeType isAuto;

    /**
     * 二维码内容
     *
     * @since 0.1
     */
    private String payUrl;
}
