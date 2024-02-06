package com.synway.vpay.bean;

import com.synway.vpay.enums.MonitorState;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

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
    private UUID id;
    /**
     * 通讯密钥
     *
     * @since 0.1
     */
    private String keyword;
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

    @SuppressWarnings("CopyConstructorMissesField")
    public AccountState(AccountState another) {
        this.id = another.getId();
        this.lastHeart = another.getLastHeart();
        this.lastPay = another.getLastPay();
        this.monitorState = another.getMonitorState();
    }

    public AccountState(UUID id) {
        this.id = id;
    }
}
