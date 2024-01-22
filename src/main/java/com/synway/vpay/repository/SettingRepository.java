package com.synway.vpay.repository;

import com.synway.vpay.base.repository.BaseRepository;
import com.synway.vpay.entity.Setting;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class SettingRepository extends BaseRepository<Setting, Long> {

    public SettingRepository(EntityManager entityManager) {
        super(Setting.class, entityManager);
    }

    public Setting findByKey(String keyword) {
        return entityManager.createQuery("select s from Setting s where s.keyword = :keyword", Setting.class)
                .setParameter("keyword", keyword)
                .getSingleResult();
    }
}
