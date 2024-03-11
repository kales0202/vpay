package com.synway.vpay.util;

import java.util.UUID;

/**
 * vpay相关常量
 *
 * @since 0.1
 */
public class VpayConstant {

    /**
     * 超级管理员账户名
     *
     * @since 0.1
     */
    public static final String SUPER_ACCOUNT = "admin";

    /**
     * 默认监控端ID
     *
     * @since 0.1
     */
    public static final UUID DEFAULT_MONITOR_ID = UUID.fromString("3237ae4e-7b13-48fd-8b60-4d9c3e7b37cd");

    public static final String ACTIVE = "active";

    public static final String DEFAULT = "default";

    public static final String HEADER_ACCOUNT = "Vpay-Account";

    public static final String HEADER_TIME = "Vpay-Time";

    public static final String HEADER_SIGN = "Vpay-Sign";

    public static final String HEADER_MONITOR = "Vpay-Monitor";
}
