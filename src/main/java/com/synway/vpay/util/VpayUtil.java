package com.synway.vpay.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.Advised;
import org.springframework.util.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class VpayUtil {

    public static String md5(String text) {
        return DigestUtils.md5DigestAsHex(text.getBytes());
    }

    public static String generateOrderId() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        return formatter.format(currentTime) + (int) (1000 + Math.random() * (9999 - 1000 + 1));
    }

    @SuppressWarnings("unchecked")
    public static <T> T getTargetBean(T bean) {
        T targetBean = bean;
        if (targetBean instanceof Advised) {
            try {
                targetBean = (T) ((Advised) targetBean).getTargetSource().getTarget();
            } catch (Exception e) {
                log.error("", e);
            }
        }
        return targetBean;
    }
}
