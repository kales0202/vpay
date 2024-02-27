package com.synway.vpay.base.define;

import jakarta.persistence.AttributeConverter;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.convert.converter.Converter;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;

/**
 * 枚举定义接口
 *
 * @since 0.1
 */
public interface IBaseEnum {

    static <T extends IBaseEnum> T fromValue(Class<T> enumType, Integer value) {
        if (Objects.isNull(value)) {
            return null;
        }
        T[] enums = enumType.getEnumConstants();
        for (T item : enums) {
            if (Objects.equals(item.getValue(), value)) {
                return item;
            }
        }
        return null;
    }

    /**
     * @return 枚举显示名称
     * @since 0.1
     */
    String getName();

    /**
     * @return 枚举值
     * @since 0.1
     */
    int getValue();

    /**
     * 枚举-数据库数据转换器JPA接口
     *
     * @param <T> 枚举类型
     * @since 0.1
     */
    abstract class BaseEnumConverter<T extends IBaseEnum> implements AttributeConverter<T, Integer>, Converter<String, T> {

        private Class<T> tClass;

        @Override
        public final Integer convertToDatabaseColumn(T attribute) {
            if (Objects.isNull(attribute)) {
                return null;
            }
            return attribute.getValue();
        }

        @Override
        public final T convertToEntityAttribute(Integer value) {
            if (Objects.isNull(value)) {
                return null;
            }
            T[] enums = this.getTClass().getEnumConstants();
            for (T item : enums) {
                if (Objects.equals(item.getValue(), value)) {
                    return item;
                }
            }
            return null;
        }

        @Override
        public final T convert(String value) {
            if (Strings.isBlank(value)) {
                return null;
            }
            Class<T> tClass = this.getTClass();
            T[] enums = tClass.getEnumConstants();
            for (T item : enums) {
                if (Objects.equals(item.getValue(), Integer.parseInt(value))) {
                    return item;
                }
            }
            throw new IllegalArgumentException("非法枚举值: class: " + tClass.getName() + ", value: " + value);
        }

        @SuppressWarnings("unchecked")
        public final Class<T> getTClass() {
            if (Objects.isNull(tClass)) {
                tClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                if (!tClass.isEnum()) {
                    throw new IllegalArgumentException("非枚举类型: " + tClass.getName());
                }
            }
            return tClass;
        }
    }
}
