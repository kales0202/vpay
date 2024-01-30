package com.synway.vpay.repository;

import com.synway.vpay.entity.Order;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class OrderRepositoryTest {

    @Resource
    private OrderRepository orderRepository;

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
    }

    @Test
    public void testFindById() {
        Order order = getOrder();
        orderRepository.save(order);
        Order result = orderRepository.findById(order.getId()).get();
        assertEquals(order.getId(), result.getId());
    }

    private Order getOrder() {
        Order order = new Order();
        order.setOrderId("123");
        return order;
    }

}