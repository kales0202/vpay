package com.synway.vpay.repository;

import com.synway.vpay.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID>, JpaSpecificationExecutor<Account> {

    /**
     * 根据账户名查找账号
     *
     * @param name 账户名
     * @return 账号信息
     * @since 0.1
     */
    Account findByName(String name);
}
