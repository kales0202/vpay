package com.synway.vpay.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.synway.vpay.base.define.IBaseEnum;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

/**
 * 支付二维码类型
 *
 * @since 0.1
 */
public enum PayQRCodeType implements IBaseEnum {

    /**
     * 固定二维码
     *
     * @since 0.1
     */
    STATIC(0, "固定二维码"),

    /**
     * 通用二维码
     *
     * @since 0.1
     */
    GENERIC(1, "通用二维码");

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

    PayQRCodeType(int value, String name) {
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

    @Component // 必须加上@Component以支持此枚举作为GET参数
    @Converter(autoApply = true)
    public static class PayQRCodeTypeConverter extends BaseEnumConverter<PayQRCodeType> {
    }
}
