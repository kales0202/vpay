package com.synway.vpay.bean;

import lombok.Data;

@Data
public class SettingVo {

    // 管理员账号
    private String user;

    // 管理员密码
    private String pass;

    // 通讯密钥
    private String key;

    // 异步通知地址
    private String notifyUrl;

    // 同步通知地址
    private String returnUrl;

    // 监控端最后心跳
    private String lastHeart;

    // 监控端最后收款
    private String lastPay;

    // 监控端状态
    private String jkState;

    // 微信通用收款码
    private String wxPay;

    // 支付宝通用收款码
    private String aliPay;

    // 区分方式 1-金额递增 2-金额递减
    private String payQf;

    // 订单有效时间
    private String close;
}
