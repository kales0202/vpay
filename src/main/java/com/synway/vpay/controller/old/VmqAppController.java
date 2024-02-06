package com.synway.vpay.controller.old;

import com.synway.vpay.base.bean.Result;
import com.synway.vpay.base.exception.BusinessException;
import com.synway.vpay.base.exception.SignatureException;
import com.synway.vpay.entity.Account;
import com.synway.vpay.enums.MonitorState;
import com.synway.vpay.service.AccountService;
import com.synway.vpay.service.OrderService;
import com.synway.vpay.util.VpayConstant;
import com.synway.vpay.util.VpayUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 旧的VMQ接口
 *
 * @since 0.1
 */
@RestController
@Deprecated
public class VmqAppController {

    @Resource
    private AccountService accountService;

    @Resource
    private OrderService orderService;

    @Resource
    private Account account;

    /**
     * 监控APP心跳检测
     *
     * @param t    时间戳
     * @param sign 签名
     * @return Void
     */
    @RequestMapping("/appHeart")
    public Result<Void> appHeart(String t, String sign) {
        if (t == null) {
            return Result.error("请传入t");
        }
        if (sign == null) {
            return Result.error("请传入sign");
        }
        // 校验签名
        this.simulatedLogin(ac -> {
            if (!Objects.equals(VpayUtil.md5(t + ac.getKeyword()), sign)) {
                throw new SignatureException();
            }
        });
        LocalDateTime appTime = VpayUtil.toDatetime(t);
        long difference = VpayUtil.getDatetimeDifference(appTime, LocalDateTime.now());
        if (difference > 60 * 1000) {
            throw new BusinessException("客户端时间错误");
        }
        // 更新监控端状态
        VpayUtil.updateMonitorState(account.getId(), MonitorState.ONLINE, appTime);
        return this.success();
    }

    @RequestMapping("/appPush")
    public Result<Void> appPush(Integer type, String price, String t, String sign) {
        return this.success();
    }

    public void simulatedLogin(Consumer<Account> consumer) {
        Account ac = accountService.findById(VpayConstant.SUPER_ID);
        consumer.accept(ac);
        account.copyFrom(ac);
    }

    private <T> Result<T> success() {
        return this.success(null);
    }

    private <T> Result<T> success(T data) {
        Result<T> success = Result.success(data);
        success.setCode(1); // vmq接口返回1表示成功
        return success;
    }
}
