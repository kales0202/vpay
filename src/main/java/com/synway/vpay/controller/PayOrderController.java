package com.synway.vpay.controller;

import com.synway.vpay.base.bean.PageData;
import com.synway.vpay.base.bean.Result;
import com.synway.vpay.bean.PayOrderBO;
import com.synway.vpay.entity.PayOrder;
import com.synway.vpay.service.PayOrderService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/order")
public class PayOrderController {

    @Resource
    private PayOrderService payOrderService;

    /**
     * 根据订单ID获取订单数据
     *
     * @param id 订单ID
     * @since 0.1
     */
    @GetMapping("/{id}")
    public Result<PayOrder> get(@PathVariable UUID id) {
        return Result.success(payOrderService.findById(id));
    }

    /**
     * 分页获取订单数据列表
     *
     * @param payOrderBO 订单查询参数
     * @since 0.1
     */
    @PostMapping("/list")
    public Result<PageData<PayOrder>> orders(@RequestBody PayOrderBO payOrderBO) {
        return Result.success(payOrderService.findAll(payOrderBO));
    }
}
