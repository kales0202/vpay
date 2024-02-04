package com.synway.vpay.old;

import com.synway.vpay.base.bean.Result;
import com.synway.vpay.bean.AccountState;
import com.synway.vpay.entity.Account;
import com.synway.vpay.service.AccountService;
import com.synway.vpay.util.VpayConstant;
import com.synway.vpay.util.VpayUtil;
import jakarta.annotation.Resource;
import org.apache.logging.log4j.util.Strings;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 旧的VMQ接口，用于兼容旧版本，便于无缝迁移
 *
 * @since 0.1
 */
@Validated
@RestController
@RequestMapping
@Deprecated
public class VmqController {

    @Resource
    private AccountService accountService;

    /**
     * 查询服务端状态（如果不传入name参数，则默认获取超级管理员的状态）
     *
     * @param t    现行时间戳
     * @param sign md5(现行时间戳+通讯密钥)
     * @return 服务端状态
     * @see for TODO... 需要补上新的地址
     * @since 0.1
     */
    @RequestMapping("/getState")
    public Result<Map<String, Object>> getState(String t, String sign, String name) {
        if (t == null) {
            return Result.error("请传入t");
        }
        if (sign == null) {
            return Result.error("请传入sign");
        }

        if (Strings.isBlank(name)) {
            name = VpayConstant.SUPER_ACCOUNT;
        }
        Account superAdmin = accountService.findByName(name);

        // 校验签名
        String checkSign = VpayUtil.md5(t + superAdmin.getKeyword());
        if (!Objects.equals(sign, checkSign)) {
            return Result.error("签名校验不通过");
        }

        AccountState state = VpayUtil.getAccountState(superAdmin.getId().toString());
        // TODO... lastpay、lastheart转时间戳（13位）
        Map<String, Object> data = new HashMap<>();
        data.put("lastpay", VpayUtil.toTimestamp(state.getLastPay()));
        data.put("lastheart", VpayUtil.toTimestamp(state.getLastHeart()));
        data.put("state", state.getMonitorState().getValue());
        return Result.success(data);
    }
}
