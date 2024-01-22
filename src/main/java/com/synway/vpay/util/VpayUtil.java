package com.synway.vpay.util;

import org.springframework.util.DigestUtils;

public class VpayUtil {

    public static String md5(String text) {
        return DigestUtils.md5DigestAsHex(text.getBytes());
    }
}
