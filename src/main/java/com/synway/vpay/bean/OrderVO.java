package com.synway.vpay.bean;

import com.synway.vpay.entity.Account;
import com.synway.vpay.entity.Order;
import com.synway.vpay.enums.OrderState;
import com.synway.vpay.enums.PayQRCodeType;
import com.synway.vpay.enums.PayType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class OrderVO {

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
    private BigDecimal price;
    /**
     * 实际支付价格
     *
     * @since 0.1
     */
    private BigDecimal reallyPrice;
    /**
     * 二维码内容
     *
     * @since 0.1
     */
    private String payUrl;
    /**
     * 支付二维码类型
     *
     * @since 0.1
     */
    private PayQRCodeType isAuto;
    /**
     * 订单状态
     *
     * @since 0.1
     */
    private OrderState state;
    /**
     * 创建时间
     *
     * @since 0.1
     */
    private LocalDateTime createTime;
    /**
     * 订单有效时间，单位分钟
     *
     * @mock 5
     * @since 0.1
     */
    private int close;

    public OrderVO(Account account, Order order) {
        this.orderId = order.getOrderId();
        this.payId = order.getPayId();
        this.payType = order.getPayType();
        this.price = order.getPrice();
        this.reallyPrice = order.getReallyPrice();
        this.payUrl = order.getPayUrl();
        this.isAuto = order.getIsAuto();
        this.state = order.getState();
        this.createTime = order.getCreateTime();
        this.close = account.getClose();
    }
}
