package com.synway.vpay.bean;

import com.synway.vpay.base.bean.SignBo;
import com.synway.vpay.enums.PayType;
import com.synway.vpay.util.VpayUtil;
import lombok.Data;

import java.math.BigDecimal;

/**
 * APP收款通知推送入参
 *
 * @since 0.1
 */
@Data
public class AppPushBO implements SignBo {

    /**
     * 支付方式
     *
     * @since 0.1
     */
    private PayType type;

    /**
     * 支付金额
     *
     * @since 0.1
     */
    private BigDecimal price;

    @Override
    public String calculateSign(String time, String key) {
        return VpayUtil.md5(this.type.getValue() + this.price.toString() + time + key);
    }
}
