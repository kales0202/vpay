package com.synway.vpay.bean;

import com.synway.vpay.entity.Order;
import com.synway.vpay.enums.PayType;
import com.synway.vpay.util.VpayUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 订单创建入参
 *
 * @since 0.1
 */
@Data
public class OrderCreateBO {

    /**
     * 商户订单号
     *
     * @since 0.1
     */
    @NotBlank
    private String payId;

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
    @NotNull
    private PayType type;

    /**
     * 订单价格
     *
     * @since 0.1
     */
    @NotNull
    @PositiveOrZero
    private BigDecimal price;

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
     * 签名认证 签名方式为 md5(payId + param + type + price + 通讯密钥)
     *
     * @since 0.1
     */
    @NotBlank
    private String sign;

    /**
     * 是否为页面级接口， 0-返回json数据，1-跳转到支付页面
     *
     * @since 0.1
     */
    private String isHtml;

    public boolean openHtml() {
        return !Objects.equals(isHtml, "1");
    }

    public Order toOrder() {
        Order order = new Order();
        order.setOrderId(VpayUtil.generateOrderId());
        order.setPayId(payId);
        order.setParam(param);
        order.setType(type);
        order.setPrice(price);
        order.setNotifyUrl(notifyUrl);
        order.setReturnUrl(returnUrl);
        return order;
    }
}
