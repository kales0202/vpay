package com.synway.vpay.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.synway.vpay.base.define.IBaseEnum;
import jakarta.persistence.Converter;

/**
 * 支付方式
 *
 * @since 0.1
 */
public enum PayType implements IBaseEnum {

    /**
     * 微信
     *
     * @since 0.1
     */
    WECHAT(1, "微信"),

    /**
     * 支付宝
     *
     * @since 0.1
     */
    ALIPAY(2, "支付宝");

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

    PayType(int value, String name) {
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
    public static class PayTypeConverter extends BaseEnumConverter<PayType> {
    }
}
