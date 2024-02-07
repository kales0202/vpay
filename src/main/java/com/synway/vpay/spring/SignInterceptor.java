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
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Component
public class SignInterceptor implements HandlerInterceptor {

    @Resource
    private Account account;

    @Resource
    private AccountService accountService;


    @Override
    @SuppressWarnings("NullableProblems")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String name = request.getHeader("vpay-name");
        String time = request.getHeader("vpay-time");
        String sign = request.getHeader("vpay-sign");
        if (Strings.isBlank(name)) {
            name = VpayConstant.SUPER_ACCOUNT;
        }
        if (Strings.isBlank(time) || Strings.isBlank(sign)) {
            throw new SignatureException();
        }
        LocalDateTime dateTime = VpayUtil.toDatetime(time);
        // 如果时间小于或者超过当前时间3分钟，则抛出异常
        long difference = VpayUtil.getDatetimeDifference(dateTime, LocalDateTime.now());
        if (difference > 3 * 60 * 1000) {
            throw new SignatureException("请求已过期...");
        }
        Account db = accountService.findByName(name);
        sign = sign.toUpperCase();
        String checkSign = VpayUtil.md5(time + db.getKeyword()).toUpperCase();
        if (!Objects.equals(sign, checkSign)) {
            throw new SignatureException();
        }
        account.copyFrom(db);
        return true;
    }

    public void registry(InterceptorRegistry registry) {
        registry.addInterceptor(this)
                .addPathPatterns("/sign/**");
    }
}
