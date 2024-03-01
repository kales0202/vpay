package com.synway.vpay.bean;

import com.synway.vpay.entity.PayCode;
import com.synway.vpay.enums.PayCodeType;
import com.synway.vpay.enums.PayType;
import lombok.Data;

import java.util.Objects;
import java.util.UUID;

/**
 * 付款码查询入参
 *
 * @since 0.1
 */
@Data
public class PayCodeBO {

    /**
     * 付款码ID
     *
     * @since 0.1
     */
    private UUID id;

    /**
     * 付款码名称
     *
     * @since 0.1
     */
    private String name;

    /**
     * 付款码类型
     *
     * @since 0.1
     */
    private PayType payType;

    /**
     * 付款码类别
     *
     * @since 0.1
     */
    private PayCodeType codeType;

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
    private Boolean enabled;

    public void toPayCode(PayCode payCode) {
        if (Objects.nonNull(name)) {
            payCode.setName(name);
        }
        if (Objects.nonNull(payType)) {
            payCode.setPayType(payType);
        }
        if (Objects.nonNull(codeType)) {
            payCode.setCodeType(codeType);
        }
        if (Objects.nonNull(enabled)) {
            payCode.setEnabled(enabled);
        }
        if (Objects.nonNull(weight)) {
            payCode.setWeight(weight);
        }
    }
}
