package com.synway.vpay.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.synway.vpay.base.entity.BaseEntity;
import com.synway.vpay.enums.PayType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

/**
 * 付款码
 *
 * @since 0.1
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class PayCode extends BaseEntity {

    /**
     * 账户ID
     *
     * @since 0.1
     */
    private UUID accountId;

    /**
     * 唯一标识名称
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
    private PayType type;

    /**
     * 付款码内容
     *
     * @since 0.1
     */
    @NotBlank
    private String payment;

    /**
     * 权重
     *
     * @since 0.1
     */
    private int weight = 1;

    /**
     * 是否启用
     *
     * @since 0.1
     */
    private boolean enable = true;

    /**
     * 所属监控端
     *
     * @since 0.1
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Monitor monitor;

    /**
     * 监控端ID(一对多关系的映射字段，需要设置@Column属性的insertable和updatable为false)
     *
     * @since 0.1
     */
    @Column(name = "monitor_id", insertable = false, updatable = false)
    private UUID monitorId;
}
