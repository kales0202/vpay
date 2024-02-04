package com.synway.vpay.bean;

import com.synway.vpay.enums.OrderState;
import com.synway.vpay.enums.PayType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


/**
 * 订单删除入参
 *
 * @since 0.1
 */
@Data
@NoArgsConstructor
public class OrderDeleteBO {

    /**
     * ID
     *
     * @since 0.1
     */
    private List<UUID> ids;

    /**
     * 时间范围-开始时间
     *
     * @since 0.1
     */
    private LocalDateTime startTime;

    /**
     * 时间范围-结束时间
     *
     * @since 0.1
     */
    private LocalDateTime endTime;

    /**
     * 支付类型
     *
     * @since 0.1
     */
    private PayType type;

    /**
     * 订单状态
     *
     * @since 0.1
     */
    private OrderState state;

    public OrderDeleteBO(List<UUID> ids) {
        this.ids = ids;
    }

    public OrderDeleteBO(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public OrderDeleteBO(PayType type) {
        this.type = type;
    }

    public OrderDeleteBO(OrderState state) {
        this.state = state;
    }
}
