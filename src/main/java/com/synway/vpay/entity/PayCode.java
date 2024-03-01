package com.synway.vpay.entity;

import com.synway.vpay.base.entity.BaseEntity;
import com.synway.vpay.enums.PayCodeType;
import com.synway.vpay.enums.PayType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

/**
 * 付款付款码
 *
 * @since 0.1
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "PAY_CODE_UNIQUE_NAME", columnNames = {"account_id", "name",}),
        @UniqueConstraint(name = "PAY_CODE_UNIQUE_CODE", columnNames = {"account_id", "content"}),
})
public class PayCode extends BaseEntity {

    /**
     * 所属账户ID
     *
     * @since 0.1
     */
    @NotNull
    private UUID accountId;

    /**
     * 付款码名称，唯一标识
     *
     * @since 0.1
     */
    @NotBlank
    private String name;

    /**
     * 付款码类型
     *
     * @since 0.1
     */
    @NotNull
    private PayType payType;

    /**
     * 付款码内容
     *
     * @since 0.1
     */
    @NotBlank
    private String content;

    /**
     * 付款码类别
     *
     * @since 0.1
     */
    private PayCodeType codeType = PayCodeType.GENERIC;

    /**
     * 权重，默认为1
     *
     * @since 0.1
     */
    private int weight = 1;

    /**
     * 是否启用，默认启用
     *
     * @since 0.1
     */
    private boolean enabled = true;
}
