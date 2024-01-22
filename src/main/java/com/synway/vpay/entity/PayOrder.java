package com.synway.vpay.entity;

import com.synway.vpay.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class PayOrder extends BaseEntity {

    // 支付云端唯一订单号
    private String orderId;

    // 支付商户订单号
    private String payId;

    // 支付时间
    private LocalDateTime payTime;

    // 关闭时间
    private LocalDateTime closeTime;

    // 订单自定义参数，会原封返回给异步接口和同步接口
    private String param;

    // 支付类型 1：微信 2：支付宝
    private int type;

    // 订单价格
    private double price;

    // 实际支付价格
    private double reallyPrice;

    // 异步地址
    private String notifyUrl;

    // 支付完成后跳转地址
    private String returnUrl;

    // 订单状态  -1：订单过期 0：等待支付 1：支付成功
    private int state;

    // 是否为通用二维码，1为通用二维码 0为固定转账二维码
    private int isAuto;

    // 二维码内容
    private String payUrl;
}
