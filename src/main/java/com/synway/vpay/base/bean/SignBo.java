package com.synway.vpay.base.bean;

public interface SignBo {

    /**
     * 调用此方法计算数据签名
     *
     * @param key 密钥
     * @return 计算出来的签名，用于进行验签，比较是否和“sign”值是否一致
     * @since 0.1
     */
    String calculateSign(String time, String key);
}
