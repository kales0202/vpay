package com.synway.vpay.bean;

import com.synway.vpay.entity.PayCode;
import com.synway.vpay.enums.PayType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

/**
 * 付款码保存/修改入参
 *
 * @since 0.1
 */
@Data
@NoArgsConstructor
public class PayCodeBO {

    /**
     * 付款码ID
     *
     * @since 0.1
     */
    @NotNull
    private UUID id;

    /**
     * 唯一标识名称
     *
     * @since 0.1
     */
    private String name;

    /**
     * 付款码类型
     *
     * @since 0.1
     */
    private PayType type;

    /**
     * 付款码内容
     *
     * @since 0.1
     */
    private String payment;

    /**
     * 权重
     *
     * @since 0.1
     */
    private Integer weight;

    /**
     * 是否启用
     *
     * @since 0.1
     */
    private Boolean enable;

    public PayCode merge2PayCode(PayCode payCode) {
        if (Objects.nonNull(name)) {
            payCode.setName(name);
        }
        if (Objects.nonNull(type)) {
            payCode.setType(type);
        }
        if (Objects.nonNull(payment)) {
            payCode.setPayment(payment);
        }
        if (Objects.nonNull(weight)) {
            payCode.setWeight(weight);
        }
        if (Objects.nonNull(enable)) {
            payCode.setEnable(enable);
        }
        return payCode;
    }
}
