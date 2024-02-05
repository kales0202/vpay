package com.synway.vpay.service;

import com.synway.vpay.base.exception.BusinessException;
import com.synway.vpay.entity.Account;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

@Slf4j
@Service
@Validated
public class AdminService {

    @Resource
    private AccountService accountService;

    public Account login(String name, String pass) {
        if (Strings.isBlank(name) || Strings.isBlank(pass)) {
            throw new BusinessException("请输入账号和密码！");
        }

        Account db = accountService.findByName(name);
        if (!Objects.equals(name, db.getName()) || !Objects.equals(pass, db.getPassword())) {
            throw new BusinessException("账号或密码不正确！");
        }
        return db;
    }

}
