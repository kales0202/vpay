package com.synway.vpay.spring;

import com.synway.vpay.base.exception.SignatureException;
import com.synway.vpay.entity.Account;
import com.synway.vpay.service.AccountService;
import com.synway.vpay.util.VpayConstant;
import com.synway.vpay.util.VpayUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Slf4j
@Component
public class PubInterceptor implements HandlerInterceptor {

    @Resource
    private Account account;

    @Resource
    private AccountService accountService;


    @Override
    @SuppressWarnings("NullableProblems")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!request.getRequestURI().startsWith("/public/") || Strings.isNotBlank(account.getName())) {
            return true;
        }
        String name = request.getHeader("vpay-name");
        String time = request.getHeader("vpay-time");
        String sign = request.getHeader("vpay-sign");
        if (Strings.isBlank(name)) {
            name = VpayConstant.SUPER_ACCOUNT;
        }
        if (Strings.isBlank(time) || Strings.isBlank(sign)) {
            throw new SignatureException();
        }
        Account db = accountService.findByName(name);
        String checkSign = VpayUtil.md5(time + db.getKeyword());
        if (!Objects.equals(sign, checkSign)) {
            throw new SignatureException();
        }
        account.copyFrom(db);
        return true;
    }
}
