package com.synway.vpay.repository;

import com.synway.vpay.base.repository.BaseRepository;
import com.synway.vpay.entity.Account;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class AccountRepository extends BaseRepository<Account, UUID> {

    public AccountRepository(EntityManager entityManager) {
        super(Account.class, entityManager);
    }

    public Account findByName(String name) {
        return entityManager.createQuery("select s from Account s where s.name = :name", Account.class)
                .setParameter("name", name)
                .getSingleResult();
    }
}
