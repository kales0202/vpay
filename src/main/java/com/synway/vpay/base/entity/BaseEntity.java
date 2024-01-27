package com.synway.vpay.base.entity;

import com.synway.vpay.base.annotation.BaseUuidGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 实体基类
 *
 * @since 0.1
 */
@Data
@MappedSuperclass
public abstract class BaseEntity {

    /**
     * ID
     *
     * @since 0.1
     */
    @Id
    @BaseUuidGenerator
    private UUID id;

    /**
     * 创建时间
     *
     * @since 0.1
     */
    @Column(updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime createTime;

    /**
     * 修改时间
     *
     * @since 0.1
     */
    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updateTime;

    // /**
    //  * 是否已被删除
    //  */
    // private boolean deleted = Boolean.FALSE;
}
