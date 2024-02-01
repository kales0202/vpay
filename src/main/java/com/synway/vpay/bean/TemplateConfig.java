package com.synway.vpay.bean;

import lombok.Data;

/**
 * 模板配置信息
 *
 * @since 0.1
 */
@Data
public class TemplateConfig {

    public static final TemplateConfig DEFAULT = new TemplateConfig();

    static {
        DEFAULT.setId("8c4c0f7d-c627-4364-81fc-38a00e735d42");
        DEFAULT.setName("默认模板");
        DEFAULT.setLogin("login.html");
        DEFAULT.setIndex("index.html");
        DEFAULT.setPay("page/pay.html");
        DEFAULT.setNotFound("404.html");
        DEFAULT.setDesc("这是默认模板");
    }

    /**
     * 模板ID
     *
     * @since 0.1
     */
    private String id;

    /**
     * 模板名称
     *
     * @since 0.1
     */
    private String name;

    /**
     * 登录页地址
     *
     * @since 0.1
     */
    private String login;

    /**
     * 主页地址
     *
     * @since 0.1
     */
    private String index;

    /**
     * 支付页地址
     */
    private String pay;

    /**
     * 404地址
     *
     * @since 0.1
     */
    private String notFound;

    /**
     * 模板描述
     *
     * @since 0.1
     */
    private String desc;
}
