package com.synway.vpay.bean;

import com.synway.vpay.enums.MonitorState;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 账户状态信息
 *
 * @since 0.1
 */
@Data
@NoArgsConstructor
public class AccountState {

    /**
     * 账户ID
     *
     * @since 0.1
     */
    private String id;

    /**
     * 监控端最后心跳
     *
     * @since 0.1
     */
    private LocalDateTime lastHeart;

    /**
     * 监控端最后收款
     *
     * @since 0.1
     */
    private LocalDateTime lastPay;

    /**
     * 监控端状态
     *
     * @since 0.1
     */
    private MonitorState monitorState = MonitorState.UNBOUND;

    public AccountState(String id) {
        this.id = id;
    }
}
