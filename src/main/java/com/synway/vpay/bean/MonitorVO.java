package com.synway.vpay.bean;

import com.synway.vpay.entity.Monitor;
import com.synway.vpay.entity.PayCode;
import com.synway.vpay.enums.MonitorState;
import com.synway.vpay.enums.PayType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 监控端保存/修改入参
 *
 * @since 0.1
 */
@Data
@NoArgsConstructor
public class MonitorVO {

    /**
     * ID
     *
     * @since 0.1
     */
    private UUID id;

    /**
     * 创建时间
     *
     * @since 0.1
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     *
     * @since 0.1
     */
    private LocalDateTime updateTime;

    /**
     * 所属账户ID
     *
     * @since 0.1
     */
    private UUID accountId;

    /**
     * 唯一标识名称
     *
     * @since 0.1
     */
    private String name;

    /**
     * 是否启用
     *
     * @since 0.1
     */
    private boolean enable = true;

    /**
     * 监控端状态
     *
     * @since 0.1
     */
    private MonitorState state = MonitorState.UNBOUND;

    /**
     * 最后心跳时间
     *
     * @since 0.1
     */
    private LocalDateTime lastHeart;

    /**
     * 最后收款时间
     *
     * @since 0.1
     */
    private LocalDateTime lastPay;

    /**
     * 微信付款码信息
     *
     * @since 0.1
     */
    private PayCode wechat;

    /**
     * 支付宝付款码信息
     *
     * @since 0.1
     */
    private PayCode alipay;

    /**
     * Monitor转MonitorVO
     *
     * @param monitor 监控端db对象
     * @return MonitorVO
     */
    public static MonitorVO from(Monitor monitor) {
        MonitorVO vo = new MonitorVO();
        vo.setId(monitor.getId());
        vo.setCreateTime(monitor.getCreateTime());
        vo.setUpdateTime(monitor.getUpdateTime());
        vo.setAccountId(monitor.getAccountId());
        vo.setName(monitor.getName());
        vo.setEnable(monitor.isEnable());
        vo.setState(monitor.getState());
        vo.setLastHeart(monitor.getLastHeart());
        vo.setLastPay(monitor.getLastPay());

        // 付款码
        List<PayCode> payCodes = monitor.getPayCodes();
        payCodes.stream().filter(p -> p.getType() == PayType.WECHAT).findFirst().ifPresent(vo::setWechat);
        payCodes.stream().filter(p -> p.getType() == PayType.ALIPAY).findFirst().ifPresent(vo::setAlipay);
        return vo;
    }
}
