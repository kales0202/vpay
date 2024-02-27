package com.synway.vpay.util;

import com.synway.vpay.bean.TemplateConfig;
import com.synway.vpay.spring.TemplateRunner;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;

import java.beans.PropertyDescriptor;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * vpay工具类
 *
 * @since 0.1
 */
@Slf4j
public class VpayUtil {

    /**
     * 计算文本的md5值
     *
     * @param text 目标文本
     * @return md5值
     * @since 0.1
     */
    public static String md5(String text) {
        return DigestUtils.md5DigestAsHex(text.getBytes());
    }

    /**
     * BCrypt加密
     *
     * @param text 待加密的文本
     * @return 加密数据
     * @since 0.1
     */
    public static String jbEncrypt(String text) {
        return BCrypt.hashpw(text, BCrypt.gensalt());
    }

    /**
     * BCrypt校验
     *
     * @param text   待验证的文本
     * @param hashed 校验数据
     * @return 校验结果
     * @since 0.1
     */
    public static boolean jbVerify(String text, String hashed) {
        return BCrypt.checkpw(text, hashed);
    }

    public static TemplateConfig getTemplateConfig() {
        return TemplateRunner.CONFIGS.get(VpayConstant.ACTIVE);
    }

    /**
     * 创建订单时，生成订单ID
     *
     * @return 订单ID
     * @since 0.1
     */
    public static String generateOrderId() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        return formatter.format(currentTime) + (int) (1000 + Math.random() * (9999 - 1000 + 1));
    }

    /**
     * 获取spring代理对象的目标对象
     *
     * @param bean spring代理对象
     * @param <T>  目标对象类型
     * @return 目标对象
     * @since 0.1
     */
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

    public static String[] getEmptyPropertyNames(Object source) {
        BeanWrapper wrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] descriptors = wrapper.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor descriptor : descriptors) {
            Object srcValue = wrapper.getPropertyValue(descriptor.getName());
            if (ObjectUtils.isEmpty(srcValue)) {
                emptyNames.add(descriptor.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static void copyNonNullProperties(Object source, Object target) {
        copyProperties(source, target, true);
    }

    public static void copyProperties(Object source, Object target, boolean ignoreNull) {
        if (ignoreNull) {
            BeanUtils.copyProperties(source, target, getEmptyPropertyNames(source));
        } else {
            BeanUtils.copyProperties(source, target);
        }
    }

    /**
     * LocalDateTime转时间戳
     *
     * @param time LocalDateTime
     * @return 时间戳
     * @since 0.1
     */
    public static Long toTimestamp(LocalDateTime time) {
        if (Objects.isNull(time)) {
            return null;
        }
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 时间戳转LocalDateTime
     *
     * @param time 时间戳
     * @return LocalDateTime
     * @since 0.1
     */
    public static LocalDateTime toDatetime(String time) {
        long timestamp = Long.parseLong(time);
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }


    /**
     * 获取两个LocalDateTime的相差毫秒数（总是返回正数）
     *
     * @param dateTime1 时间1
     * @param dateTime2 时间2
     * @return 相差毫秒数
     * @since 0.1
     */
    public static long getDatetimeDifference(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        Instant instant1 = dateTime1.atZone(ZoneId.systemDefault()).toInstant();
        Instant instant2 = dateTime2.atZone(ZoneId.systemDefault()).toInstant();
        return Duration.between(instant1, instant2).abs().toMillis();
    }
}
