package com.synway.vpay.base.define;

/**
 * 权重接口，服务于轮询策略
 *
 * @since 0.1
 */
public interface IWeight {

    /**
     * 获取实例权重值
     *
     * @return 权重值
     * @since 0.1
     */
    int getWeight();
}
