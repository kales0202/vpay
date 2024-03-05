package com.synway.vpay.base.strategy;

import com.synway.vpay.base.define.Strategy;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 权重轮询策略
 *
 * @param <T> 轮询值类型
 * @since 0.1
 */
public class WeightStrategy<T> implements Strategy<T> {

    private final AtomicInteger counter = new AtomicInteger(0);

    private final List<WeightItem<T>> items;

    private final int total;

    public WeightStrategy(List<WeightItem<T>> items) {
        this.items = items;
        this.total = items.stream().mapToInt(WeightItem::weight).sum();
    }

    @Override
    public String getName() {
        return "依据权重轮询";
    }

    @Override
    public T next() {
        int currentIndex = counter.getAndIncrement() % total;

        for (WeightItem<T> item : items) {
            currentIndex -= item.weight();
            if (currentIndex < 0) {
                return item.item();
            }
        }
        return null;
    }

    public record WeightItem<T>(T item, int weight) {
    }
}
