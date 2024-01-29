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
    OUT(0, "订单已过期"),

    /**
     * 等待支付
     *
     * @since 0.1
     */
    WAIT(1, "等待支付"),

    /**
     * 支付成功
     *
     * @since 0.1
     */
    SUCCESS(2, "支付成功");

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

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    @Converter(autoApply = true)
    public static class OrderStateConverter extends BaseEnumConverter<OrderState> {
    }
}
