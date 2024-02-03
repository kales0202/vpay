package com.synway.vpay.bean;

import com.synway.vpay.base.bean.BasePage.BasePageBO;
import com.synway.vpay.entity.Order;
import com.synway.vpay.enums.OrderState;
import com.synway.vpay.enums.PayType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 订单查询入参
 *
 * @since 0.1
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class OrderQueryBO extends BasePageBO {
    private final static Sort DESC_BY_CREATE_TIME = Sort.sort(Order.class).by(Order::getCreateTime).descending();

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

    @Override
    public Pageable getPageable() {
        return PageRequest.of(this.getPage() - 1, this.getSize(), DESC_BY_CREATE_TIME);
    }
}
