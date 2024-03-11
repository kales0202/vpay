package com.synway.vpay.bean;

import com.synway.vpay.enums.PayType;
import lombok.Data;

import java.util.UUID;

/**
 * 监控端列表查询入参
 *
 * @since 0.1
 */
@Data
public class MonitorListBO {

    /**
     * 监控端ID
     *
     * @since 0.1
     */
    private UUID id;

    /**
     * 账户ID
     *
     * @since 0.1
     */
    private UUID accountId;

    /**
     * 监控端名称
     *
     * @since 0.1
     */
    private String name;

    /**
     * 支付方式
     *
     * @since 0.1
     */
    private PayType type;

    /**
     * 是否启用
     *
     * @since 0.1
     */
    private Boolean enable;
}
