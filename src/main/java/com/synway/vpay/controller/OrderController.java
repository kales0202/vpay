package com.synway.vpay.controller;

import com.synway.vpay.base.bean.PageData;
import com.synway.vpay.base.bean.Result;
import com.synway.vpay.bean.OrderBO;
import com.synway.vpay.entity.Order;
import com.synway.vpay.service.OrderService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * 订单管理
 *
 * @since 0.1
 */
@Validated
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    /**
     * 根据订单ID获取订单数据
     *
     * @param id 订单ID
     * @since 0.1
     */
    @GetMapping("/{id}")
    public Result<Order> get(@PathVariable UUID id) {
        return Result.success(orderService.findById(id));
    }

    /**
     * 根据订单ID删除订单数据
     *
     * @param id 订单ID
     * @since 0.1
     */
    @DeleteMapping("/{id}")
    public Result<Order> delete(@PathVariable UUID id) {
        orderService.deleteById(id);
        return Result.success();
    }

    /**
     * 新建订单
     *
     * @param order 订单信息
     * @since 0.1
     */
    @PostMapping
    public Result<Void> create(@RequestBody Order order) {
        orderService.save(order);
        return Result.success();
    }

    /**
     * 分页获取订单数据列表
     *
     * @param orderBO 订单查询参数
     * @since 0.1
     */
    @PostMapping("/list")
    public Result<PageData<Order>> orders(@RequestBody OrderBO orderBO) {
        return Result.success(orderService.findAll(orderBO));
    }
}
