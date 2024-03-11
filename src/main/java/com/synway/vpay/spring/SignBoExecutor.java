package com.synway.vpay.spring;


import com.synway.vpay.base.annotation.ValidSign.Executor;
import com.synway.vpay.base.bean.SignBo;
import com.synway.vpay.base.exception.SignatureException;
import com.synway.vpay.entity.Account;
import com.synway.vpay.service.AccountService;
import com.synway.vpay.util.VpayConstant;
import com.synway.vpay.util.VpayUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class SignBoExecutor implements Executor {

    @Resource
    private AccountService accountService;

    @Resource
    private HttpServletRequest request;

    @Resource
    private Account account;

    @Override
    public void execute(SignBo bo) throws SignatureException {
        String acName = request.getHeader(VpayConstant.HEADER_ACCOUNT);
        String time = request.getHeader(VpayConstant.HEADER_TIME);
        String sign = request.getHeader(VpayConstant.HEADER_SIGN);
        if (Strings.isBlank(acName)) {
            acName = VpayConstant.SUPER_ACCOUNT;
        }
        if (Strings.isBlank(time) || Strings.isBlank(sign)) {
            throw new SignatureException("验签失败，缺少Header：" + VpayConstant.HEADER_TIME + "或" + VpayConstant.HEADER_SIGN);
        }
        LocalDateTime dateTime = VpayUtil.toDatetime(time);
        // 如果时间小于或者超过当前时间3分钟，则抛出异常
        long difference = VpayUtil.getDatetimeDifference(dateTime, LocalDateTime.now());
        if (difference > 3 * 60 * 1000) {
            throw new SignatureException("请求已过期...");
        }

        Account db = accountService.findByName(acName);

        String checkSign;
        if (Objects.isNull(bo)) {
            // 接口简单验签：时间+key计算md5值
            checkSign = VpayUtil.md5(time + db.getKeyword());
        } else {
            // 接口参数混合验签：使用实现了SignBo的入参，计算md5值
            checkSign = bo.calculateSign(time, db.getKeyword());
        }

        if (!Objects.equals(sign.toUpperCase(), checkSign.toUpperCase())) {
            throw new SignatureException();
        }

        // 验签成功，复制账户信息到account中
        account.copyFrom(db);
    }
}
