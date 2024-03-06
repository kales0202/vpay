package com.synway.vpay.base.define;

import java.util.List;

/**
 * 轮询策略接口
 *
 * @param <T> 轮询值类型
 * @since 0.1
 */
public interface Strategy<T> {

    /**
     * 创建策略实例
     *
     * @param list 策略值
     * @return 策略实例
     */
    static <T> Strategy<T> newInstance(List<T> list) {
        return new Strategy<T>() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public T next() {
                return null;
            }
        };
    }

    /**
     * 获取策略名称
     *
     * @return 策略名称
     * @since 0.1
     */
    String getName();

    /**
     * 获取策略值
     *
     * @return 策略值
     * @since 0.1
     */
    T next();
}
