package com.synway.vpay.bean;

import com.synway.vpay.base.bean.BasePage.BasePageBO;
import com.synway.vpay.entity.PayOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PayOrderBO extends BasePageBO {
    private final static Sort DESC_BY_CREATE_TIME = Sort.sort(PayOrder.class).by(PayOrder::getCreateTime).descending();

    /**
     * 支付类型
     *
     * @see PayOrder#type
     */
    private Integer type;

    /**
     * 订单状态
     *
     * @see PayOrder#state
     */
    private Integer state;

    @Override
    public Pageable getPageable() {
        return PageRequest.of(this.getPage(), this.getSize(), DESC_BY_CREATE_TIME);
    }
}
