package com.synway.vpay.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.synway.vpay.base.define.IBaseEnum;
import jakarta.persistence.Converter;

/**
 * 菜单类型
 *
 * @since 0.1
 */
public enum MenuType implements IBaseEnum {

    /**
     * 父级菜单
     *
     * @since 0.1
     */
    MENU(0),

    /**
     * url
     *
     * @since 0.1
     */
    URL(1);

    /**
     * 枚举值
     *
     * @since 0.1
     */
    @JsonValue
    private final int value;

    MenuType(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Converter(autoApply = true)
    public static class MenuTypeConverter extends BaseEnumConverter<MenuType> {
    }
}
