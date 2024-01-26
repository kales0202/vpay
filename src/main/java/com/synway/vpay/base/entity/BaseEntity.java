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

@Data
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @BaseUuidGenerator
    private UUID id;

    // 创建时间
    @Column(updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime createTime;

    // 修改时间
    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updateTime;

    // 是否已被删除
    // private boolean deleted = Boolean.FALSE;
}
