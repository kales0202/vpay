package com.synway.vpay.controller;

import com.synway.vpay.base.bean.PageData;
import com.synway.vpay.base.bean.Result;
import com.synway.vpay.bean.OrderDeleteBO;
import com.synway.vpay.bean.OrderQueryBO;
import com.synway.vpay.bean.OrderStatisticsVO;
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
     * 批量删除订单数据
     *
     * @param bo 订单删除入参
     * @return 删除条数
     * @since 0.1
     */
    @DeleteMapping
    public Result<Integer> delete(@RequestBody OrderDeleteBO bo) {
        return Result.success(orderService.delete(bo));
    }


    /**
     * 分页获取订单数据列表
     *
     * @param bo 订单查询参数
     * @since 0.1
     */
    @PostMapping("/list")
    public Result<PageData<Order>> list(@RequestBody OrderQueryBO bo) {
        return Result.success(orderService.findAll(bo));
    }

    /**
     * 获取订单统计数据
     *
     * @since 0.1
     */
    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> statistics() {
        return Result.success(orderService.statistics());
    }

    /**
     * 补单
     *
     * @return 补单失败时的异常信息
     * @since 0.1
     */
    @GetMapping("/fulfill")
    public Result<String> fulfill(UUID id) {
        return Result.success(orderService.fulfill(id));
    }
}
