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
        DEFAULT.setKey("default");
        DEFAULT.setName("默认模板");
        DEFAULT.setLogin("login.html");
        DEFAULT.setIndex("index.html");
        DEFAULT.setPay("page/pay.html");
        DEFAULT.setNotFound("404.html");
        DEFAULT.setDesc("这是默认模板");
    }

    /**
     * 模板KEY, 唯一标识
     *
     * @since 0.1
     */
    private String key;

    /**
     * 模板名称
     *
     * @since 0.1
     */
    private String name;

    /**
     * 模板所在位置：绝对路径（不包含模板名称）
     *
     * @since 0.1
     */
    private String location;

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
     *
     * @since 0.1
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
