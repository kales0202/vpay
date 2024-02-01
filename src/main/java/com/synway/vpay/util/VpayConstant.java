package com.synway.vpay.util;

import com.synway.vpay.bean.AccountState;

import java.util.HashMap;
import java.util.Map;
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
     * 超级管理员账户ID
     *
     * @since 0.1
     */
    public static UUID SUPER_ID = UUID.randomUUID();

    /**
     * 账户当前状态
     *
     * @since 0.1
     */
    public static Map<String, AccountState> ACCOUNT_STATE_MAP = new HashMap<>();
}
