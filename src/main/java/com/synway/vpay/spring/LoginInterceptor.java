package com.synway.vpay.spring;

import com.synway.vpay.base.exception.AuthorizedException;
import com.synway.vpay.util.VpayConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    @SuppressWarnings("NullableProblems")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String servletPath = request.getServletPath();
        log.debug("登录校验：{}", servletPath);
        HttpSession session = request.getSession();
        String user = (String) session.getAttribute(VpayConstant.USER);
        if (Strings.isNotBlank(user)) {
            log.debug("登录校验成功，登录用户：{}", user);
            return true;
        }
        log.debug("登录校验失败...");
        throw new AuthorizedException();
    }
}
