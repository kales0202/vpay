package com.synway.vpay.service;

import com.synway.vpay.base.exception.BusinessException;
import com.synway.vpay.base.exception.IllegalOperationException;
import com.synway.vpay.entity.Account;
import com.synway.vpay.enums.PayType;
import com.synway.vpay.repository.AccountRepository;
import com.synway.vpay.util.VpayUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.Objects;

@Slf4j
@Service
@Validated
public class AdminService {

    @Resource
    private AccountRepository accountRepository;

    @Resource
    private HttpServletRequest request;

    @Resource
    private Account account;

    public Account login(String name, String pass) {
        if (Strings.isBlank(name) || Strings.isBlank(pass)) {
            throw new BusinessException("请输入账号和密码！");
        }

        Account account = accountRepository.findByName(name);
        if (Objects.isNull(account)) {
            throw new BusinessException("账号不存在！");
        }
        if (!Objects.equals(name, account.getName()) || !Objects.equals(pass, account.getPassword())) {
            throw new BusinessException("账号或密码不正确！");
        }
        return account;
    }

    public void verifySign(@NotBlank String payId,
                           @NotBlank String param,
                           @NotNull PayType type,
                           @NotNull BigDecimal price,
                           @NotBlank String sign) {
        String cSign = VpayUtil.md5(payId + param + type.getValue() + price + account.getKeyword());
        if (!Objects.equals(sign, cSign)) {
            throw new IllegalOperationException("签名验证错误");
        }
    }
}
