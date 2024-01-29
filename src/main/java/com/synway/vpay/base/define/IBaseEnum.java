package com.synway.vpay.base.define;

import com.synway.vpay.base.exception.IllegalArgumentException;
import jakarta.persistence.AttributeConverter;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;

public interface IBaseEnum {

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

    abstract class BaseEnumConverter<T extends IBaseEnum> implements AttributeConverter<T, Integer> {

        private Class<T> tClass;

        @Override
        public final Integer convertToDatabaseColumn(T attribute) {
            return attribute.getValue();
        }

        public final T convertToEntityAttribute(Integer value) {
            T[] enums = this.getTClass().getEnumConstants();
            for (T item : enums) {
                if (Objects.equals(item.getValue(), value)) {
                    return item;
                }
            }
            return null;
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
