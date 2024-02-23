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
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private Account account;

    @Override
    @SuppressWarnings("NullableProblems")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (Strings.isNotBlank(account.getName())) {
            log.debug("登录校验成功，登录账户：{}", account.getName());
            return true;
        }
        log.debug("登录校验失败...");
        throw new AuthorizedException();
    }

    public void registry(InterceptorRegistry registry) {
        registry.addInterceptor(this)
                .excludePathPatterns(
                        "/login",
                        "/error",
                        "/account/login",
                        "/404",
                        "/**/*.html",
                        "/**/*.js",
                        "/**/*.css",
                        "/**/*.png",
                        "/**/*.jpg",
                        "/**/*.gif",
                        "/**/*.ttf",
                        "/**/*.woff",
                        "/**/*.woff2",
                        "/js/**",
                        "/css/**",
                        "/images/**",
                        "/page/**",
                        "/templates/**",
                        "/favicon.ico",
                        "/favicon",
                        // 以下为新版地址
                        "/public/**",
                        "/sign/**",
                        // 以下为vmq旧版地址
                        "/createOrder",
                        "/getState",
                        "/getOrder",
                        "/checkOrder",
                        "/closeOrder",
                        "/appHeart",
                        "/appPush"
                )
                .addPathPatterns("/**");
    }
}
