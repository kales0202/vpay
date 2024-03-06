package com.synway.vpay.enums;

import com.synway.vpay.base.define.IBaseEnum;
import com.synway.vpay.base.define.Strategy;
import com.synway.vpay.base.exception.ClassConstructorException;
import com.synway.vpay.base.strategy.RandomStrategy;
import com.synway.vpay.base.strategy.WeightStrategy;

import java.util.List;

@SuppressWarnings("rawtypes")
public enum PaymentStrategy implements IBaseEnum {

    RandomStrategy(1, "无视权重随机轮询", RandomStrategy.class),

    WeightStrategy(2, "依据权重轮询", WeightStrategy.class);

    private final int value;

    private final String name;

    private final Class<? extends Strategy> clazz;

    PaymentStrategy(int value, String name, Class<? extends Strategy> clazz) {
        this.value = value;
        this.name = name;
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    public Class<? extends Strategy> getClazz() {
        return this.clazz;
    }

    @SuppressWarnings("unchecked")
    public <T> Strategy<T> newInstance(List<T> list) {
        try {
            return this.clazz.getConstructor(List.class).newInstance(list);
        } catch (Exception e) {
            throw new ClassConstructorException("", e);
        }
    }
}
