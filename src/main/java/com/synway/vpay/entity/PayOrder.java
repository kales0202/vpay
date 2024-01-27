package com.synway.vpay.entity;

import com.synway.vpay.base.entity.BaseEntity;
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
     * 订单自定义参数，会原封返回给异步接口和同步接口
     *
     * @since 0.1
     */
    private String param;

    /**
     * 支付类型 1：微信 2：支付宝
     *
     * @since 0.1
     */
    private int type;

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
     * 订单状态  -1：订单过期 0：等待支付 1：支付成功
     *
     * @since 0.1
     */
    private int state;

    /**
     * 是否为通用二维码，1为通用二维码 0为固定转账二维码
     *
     * @since 0.1
     */
    private int isAuto;

    /**
     * 二维码内容
     *
     * @since 0.1
     */
    private String payUrl;
}
