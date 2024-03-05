package com.synway.vpay.service;

import com.synway.vpay.base.exception.IllegalArgumentException;
import com.synway.vpay.base.exception.IllegalOperationException;
import com.synway.vpay.bean.PayCodeBO;
import com.synway.vpay.entity.Account;
import com.synway.vpay.entity.Monitor;
import com.synway.vpay.entity.PayCode;
import com.synway.vpay.repository.PayCodeRepository;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@Validated
public class PayCodeService {

    @Resource
    private MonitorService monitorService;

    @Resource
    private PayCodeRepository payCodeRepository;

    @Resource
    private Account account;

    public PayCode findById(UUID id) {
        return payCodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("没有找到对应的支付码"));
    }

    public void deleteById(UUID id) {
        payCodeRepository.deleteById(id);
    }

    public void delete(PayCode payCode) {
        payCodeRepository.delete(payCode);
    }

    public PayCode create(PayCode payCode) {
        payCode.setAccountId(account.getId());

        Monitor monitor = monitorService.findById(payCode.getMonitorId());
        payCode.setMonitor(monitor);

        List<PayCode> payCodes = monitor.getPayCodes();
        payCodes.stream().filter(p -> Objects.equals(p.getType(), payCode.getType())).findFirst().ifPresent(p -> {
            throw new IllegalOperationException("该类型的支付码已存在！");
        });

        this.validateUniquePayment(payCode);

        return payCodeRepository.save(payCode);
    }

    public PayCode save(@Valid PayCodeBO bo) {
        PayCode payCode = this.findById(bo.getId());

        // 修改了付款码，需要校验付款码是否已经存在
        if (Objects.nonNull(bo.getPayment()) && !Objects.equals(bo.getPayment(), payCode.getPayment())) {
            payCode.setPayment(bo.getPayment());
            this.validateUniquePayment(payCode);
        }
        bo.merge2PayCode(payCode);

        return payCodeRepository.save(payCode);
    }

    public void validateUniquePayment(PayCode payCode) {
        PayCode existed = payCodeRepository.findByAccountIdAndIdNotAndPayment(account.getId(), payCode.getId(), payCode.getPayment());
        if (Objects.nonNull(existed)) {
            Monitor monitor = existed.getMonitor();
            throw new IllegalArgumentException("付款码已存在请勿重复添加，监控端名称为：" + monitor.getName());
        }
    }
}
