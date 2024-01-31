package com.synway.vpay.spring;

import com.synway.vpay.base.exception.AuthorizedException;
import com.synway.vpay.entity.Account;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private Account account;

    @Override
    @SuppressWarnings("NullableProblems")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (Strings.isNotBlank(account.getName())) {
            log.debug("登录校验成功，登录用户：{}", account.getName());
            return true;
        }
        log.debug("登录校验失败...");
        throw new AuthorizedException();
    }
}
