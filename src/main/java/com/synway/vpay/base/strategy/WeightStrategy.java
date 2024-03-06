package com.synway.vpay.base.strategy;

import com.synway.vpay.base.define.IWeight;
import com.synway.vpay.base.define.Strategy;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 权重轮询策略
 *
 * @since 0.1
 */
public class WeightStrategy implements Strategy<IWeight> {

    private final AtomicInteger counter = new AtomicInteger(0);

    private final List<IWeight> items;

    private final int total;

    public WeightStrategy(List<IWeight> items) {
        this.items = items;
        this.total = items.stream().mapToInt(IWeight::getWeight).sum();
    }

    @Override
    public String getName() {
        return "依据权重轮询";
    }

    @Override
    public IWeight next() {
        // counter加1，如果达到最大值，则重置为0
        int cur = counter.getAndIncrement();
        if (cur == Integer.MAX_VALUE) {
            counter.getAndSet(0);
        }

        int currentIndex = cur % total;

        for (IWeight item : items) {
            currentIndex -= item.getWeight();
            if (currentIndex < 0) {
                return item;
            }
        }
        return null;
    }
}
