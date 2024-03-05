package com.synway.vpay.entity;

import com.synway.vpay.base.entity.BaseEntity;
import com.synway.vpay.enums.MonitorState;
import com.synway.vpay.enums.PayType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.logging.log4j.util.Strings;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 监控端，包含一个微信支付码、支付宝支付码
 *
 * @since 0.1
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "MONITOR_UNIQUE_NAME", columnNames = {"account_id", "name",}),
})
public class Monitor extends BaseEntity {

    /**
     * 所属账户ID
     *
     * @since 0.1
     */
    @NotNull
    private UUID accountId;

    /**
     * 唯一标识名称
     *
     * @since 0.1
     */
    @NotBlank
    private String name;

    /**
     * 是否启用
     *
     * @since 0.1
     */
    private boolean enable = true;

    /**
     * 监控的付款码集合
     *
     * @since 0.1
     */
    @OneToMany(mappedBy = "monitor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PayCode> payCodes = new ArrayList<>();

    /**
     * 监控端状态
     *
     * @since 0.1
     */
    @Transient
    private MonitorState state = MonitorState.UNBOUND;

    /**
     * 最后心跳时间
     *
     * @since 0.1
     */
    @Transient
    private LocalDateTime lastHeart;

    /**
     * 最后收款时间
     *
     * @since 0.1
     */
    @Transient
    private LocalDateTime lastPay;

    public List<PayCode> getPayCodes(PayType type) {
        if (CollectionUtils.isEmpty(payCodes)) {
            return List.of();
        }
        return payCodes.stream()
                .filter(p -> p.getType() == type)
                .filter(PayCode::isEnable)
                .filter(p -> Strings.isNotBlank(p.getPayment()))
                .toList();
    }
}
