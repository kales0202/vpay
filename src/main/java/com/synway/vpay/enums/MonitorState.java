package com.synway.vpay.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.synway.vpay.base.define.IBaseEnum;
import jakarta.persistence.Converter;

/**
 * 监控端状态
 *
 * @since 0.1
 */
public enum MonitorState implements IBaseEnum {

    /**
     * 离线
     *
     * @since 0.1
     */
    OFFLINE(0, "离线"),

    /**
     * 在线
     *
     * @since 0.1
     */
    ONLINE(1, "在线"),

    /**
     * 未绑定
     *
     * @since 0.1
     */
    UNBOUND(-1, "未绑定");

    /**
     * 枚举值
     *
     * @since 0.1
     */
    @JsonValue
    private final int value;

    private final String name;

    MonitorState(int value, String name) {
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
    public static class MonitorStateConverter extends BaseEnumConverter<MonitorState> {
    }
}