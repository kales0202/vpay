package com.synway.vpay.base.repository;

import com.synway.vpay.base.entity.BaseEntity;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.io.Serializable;

public abstract class BaseRepository<T extends BaseEntity, ID extends Serializable> extends SimpleJpaRepository<T, ID> {

    protected final EntityManager entityManager;

    protected BaseRepository(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public void delete(T entity) {
        // 逻辑删除
        // entity.setDeleted(Boolean.TRUE);
        // this.entityManager.merge(entity);
        super.delete(entity);
    }
}