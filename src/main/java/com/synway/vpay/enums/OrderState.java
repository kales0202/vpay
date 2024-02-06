package com.synway.vpay.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.synway.vpay.base.define.IBaseEnum;
import jakarta.persistence.Converter;

/**
 * 订单状态
 *
 * @since 0.1
 */
public enum OrderState implements IBaseEnum {

    /**
     * 订单已过期
     *
     * @since 0.1
     */
    EXPIRED(-1, "订单已过期"),

    /**
     * 等待支付
     *
     * @since 0.1
     */
    WAIT(0, "等待支付"),

    /**
     * 支付成功
     *
     * @since 0.1
     */
    SUCCESS(1, "支付成功"),

    /**
     * 通知失败
     *
     * @since 0.1
     */
    NOTIFY_FAILED(2, "通知失败");

    /**
     * 枚举值
     *
     * @since 0.1
     */
    @JsonValue
    private final int value;

    /**
     * 枚举名称
     *
     * @since 0.1
     */
    private final String name;

    OrderState(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Converter(autoApply = true)
    public static class OrderStateConverter extends BaseEnumConverter<OrderState> {
    }
}
