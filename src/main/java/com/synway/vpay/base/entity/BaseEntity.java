package com.synway.vpay.base.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
