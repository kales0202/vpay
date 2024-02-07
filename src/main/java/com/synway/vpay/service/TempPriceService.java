package com.synway.vpay.service;

import com.synway.vpay.base.exception.BusinessException;
import com.synway.vpay.entity.Account;
import com.synway.vpay.entity.TempPrice;
import com.synway.vpay.enums.PayType;
import com.synway.vpay.repository.TempPriceRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@Validated
public class TempPriceService {

    private static final BigDecimal RATIO = new BigDecimal("0.01");

    @Resource
    private TempPriceRepository tempPriceRepository;

    @Resource
    private Account account;

    /**
     * 获取实际支付金额
     *
     * @param payType 支付方式
     * @param price   支付金额
     * @return 保存的实际支付金额
     * @since 0.1
     */
    public BigDecimal saveReallyPrice(UUID accountId, PayType payType, BigDecimal price) {
        TempPrice tempPrice = new TempPrice(accountId, payType, price);
        while (true) {
            try {
                tempPrice = tempPriceRepository.save(tempPrice);
                return tempPrice.getPrice();
            } catch (Exception e) {
                // do nothing
            }
            // reallyPrice +/- 0.01
            if (account.getPayQf() == 0) {
                tempPrice.setPrice(tempPrice.getPrice().add(RATIO));
            } else {
                tempPrice.setPrice(tempPrice.getPrice().subtract(RATIO));
            }
            // 判断是否小于0
            if (tempPrice.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException("所有金额均被占用");
            }
        }
    }

    /**
     * 根据支付方式和支付金额删除订单
     *
     * @param payType 支付方式
     * @param price   支付金额
     * @return int 删除的数量
     * @since 0.1
     */
    public int deleteByPayTypeAndPrice(PayType payType, BigDecimal price) {
        return tempPriceRepository.deleteByAccountIdAndPayTypeAndPrice(account.getId(), payType, price);
    }
}
