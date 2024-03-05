package com.synway.vpay.base.define;

/**
 * 轮询策略接口
 *
 * @param <T> 轮询值类型
 * @since 0.1
 */
public interface Strategy<T> {

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
