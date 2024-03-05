package com.synway.vpay.base.strategy;

import com.synway.vpay.base.define.Strategy;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Random;

/**
 * 无视权重随机轮询策略
 *
 * @param <T> 轮询值类型
 * @since 0.1
 */
public class RandomStrategy<T> implements Strategy<T> {

    private final Random random = new Random();

    private final List<T> list;

    public RandomStrategy(List<T> list) {
        this.list = list;
    }

    @Override
    public String getName() {
        return "无视权重随机轮询";
    }

    @Override
    public T next() {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(random.nextInt(list.size()));
    }
}
