package com.synway.vpay.repository;

import com.synway.vpay.base.repository.BaseRepository;
import com.synway.vpay.entity.Setting;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class SettingRepository extends BaseRepository<Setting, UUID> {

    public SettingRepository(EntityManager entityManager) {
        super(Setting.class, entityManager);
    }

    public Setting findByUsername(String username) {
        return entityManager.createQuery("select s from Setting s where s.username = :username", Setting.class)
                .setParameter("username", username)
                .getSingleResult();
    }
}
