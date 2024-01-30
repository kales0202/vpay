package com.synway.vpay.service;

import com.synway.vpay.base.bean.PageData;
import com.synway.vpay.bean.OrderBO;
import com.synway.vpay.entity.Order;
import com.synway.vpay.repository.OrderRepository;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {
    @Resource
    private OrderRepository orderRepository;

    public Order findById(UUID id) {
        return orderRepository.findById(id).orElse(null);
    }

    public void deleteById(UUID id) {
        orderRepository.deleteById(id);
    }

    public void save(Order order) {
        orderRepository.save(order);
    }

    public PageData<Order> findAll(OrderBO bo) {
        Page<Order> orders = orderRepository.findAll(bo);
        return new PageData<>(orders.getTotalElements(), bo.getPage(), bo.getSize(), orders.getContent());
    }
}
