package com.synway.vpay.entity;

import com.synway.vpay.base.entity.BaseEntity;
import com.synway.vpay.enums.PayType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

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
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "TEMP_PRICE_UNIQUE_TYPE_PRICE", columnNames = {"account_id", "pay_type", "price"})
})
public class TempPrice extends BaseEntity {

    private UUID accountId;

    private PayType payType;

    private BigDecimal price;
}
