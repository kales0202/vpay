package com.synway.vpay.service;

import com.synway.vpay.base.bean.PageData;
import com.synway.vpay.bean.PayOrderBO;
import com.synway.vpay.entity.PayOrder;
import com.synway.vpay.repository.PayOrderRepository;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PayOrderService {
    @Resource
    private PayOrderRepository payOrderRepository;

    public PayOrder findById(UUID id) {
        return payOrderRepository.findById(id).orElse(null);
    }

    public void deleteById(UUID id) {
        payOrderRepository.deleteById(id);
    }

    public PageData<PayOrder> findAll(PayOrderBO bo) {
        Page<PayOrder> orders = payOrderRepository.findAll(bo);
        return new PageData<>(orders.getTotalElements(), bo.getPage(), bo.getSize(), orders.getContent());
    }
}
