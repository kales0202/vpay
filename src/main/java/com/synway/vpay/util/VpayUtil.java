package com.synway.vpay.util;

import com.synway.vpay.bean.AccountState;
import com.synway.vpay.enums.MonitorState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.Advised;
import org.springframework.util.DigestUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

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

    /**
     * 获取账户状态
     *
     * @param id 账户ID
     * @return 账户状态信息
     * @since 0.1
     */
    public static AccountState getAccountState(String id) {
        AccountState accountState;
        if (VpayConstant.ACCOUNT_STATE_MAP.containsKey(id)) {
            accountState = VpayConstant.ACCOUNT_STATE_MAP.get(id);
        } else {
            accountState = new AccountState(id);
            VpayConstant.ACCOUNT_STATE_MAP.put(id, accountState);
        }
        return accountState;
    }

    /**
     * 更新账户信息：最后心跳
     *
     * @param id        账户ID
     * @param lastHeart 最后心跳
     * @since 0.1
     */
    public static void updateLastHeart(String id, LocalDateTime lastHeart) {
        AccountState accountState = getAccountState(id);
        accountState.setLastHeart(lastHeart);
    }

    /**
     * 更新账户信息：最后支付时间
     *
     * @param id      账户ID
     * @param lastPay 最后支付时间
     * @since 0.1
     */
    public static void updateLastPay(String id, LocalDateTime lastPay) {
        AccountState accountState = getAccountState(id);
        accountState.setLastPay(lastPay);
    }

    /**
     * 更新账户信息：监控端状态
     *
     * @param id           账户ID
     * @param monitorState 监控端状态
     * @since 0.1
     */
    public static void updateMonitorState(String id, MonitorState monitorState) {
        AccountState accountState = getAccountState(id);
        accountState.setMonitorState(monitorState);
    }
}
