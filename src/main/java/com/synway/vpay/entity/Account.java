package com.synway.vpay.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.synway.vpay.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

/**
 * 账户信息
 *
 * @since 0.1
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Account extends BaseEntity {

    /**
     * 管理员账号
     *
     * @since 0.1
     */
    @NotNull
    private String name;

    /**
     * 管理员密码
     *
     * @since 0.1
     */
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;

    /**
     * 通讯密钥
     *
     * @since 0.1
     */
    private String keyword;

    /**
     * 异步通知地址
     *
     * @since 0.1
     */
    private String notifyUrl;

    /**
     * 同步通知地址
     *
     * @since 0.1
     */
    private String returnUrl;

    /**
     * 微信通用收款码
     *
     * @since 0.1
     */
    @NotNull
    private String wxPay;

    /**
     * 支付宝通用收款码
     *
     * @since 0.1
     */
    @NotNull
    private String aliPay;

    /**
     * 区分方式 0-金额递增 1-金额递减
     *
     * @mock 0
     * @since 0.1
     */
    private int payQf = 0;

    /**
     * 订单有效时间，单位分钟，默认是5
     *
     * @mock 5
     * @since 0.1
     */
    private int close = 5;

    /**
     * 会话ID
     *
     * @since 0.1
     */
    @Transient
    @JsonIgnore
    private String sessionId;

    /**
     * 拷贝Account数据
     *
     * @param from 源
     * @since 0.1
     */
    public void copyFrom(Account from) {
        this.setId(from.getId());
        this.setName(from.getName());
        this.setPassword(from.getPassword());
        this.setKeyword(from.getKeyword());
        this.setNotifyUrl(from.getNotifyUrl());
        this.setReturnUrl(from.getReturnUrl());
        this.setWxPay(from.getWxPay());
        this.setAliPay(from.getAliPay());
        this.setPayQf(from.getPayQf());
        this.setClose(from.getClose());
        this.setCreateTime(from.getCreateTime());
        this.setUpdateTime(from.getUpdateTime());
    }
}
