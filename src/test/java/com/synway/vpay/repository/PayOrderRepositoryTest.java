package com.synway.vpay.repository;

import com.synway.vpay.entity.PayOrder;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PayOrderRepositoryTest {

    @Resource
    private PayOrderRepository payOrderRepository;

    @AfterEach
    void tearDown() {
        payOrderRepository.deleteAll();
    }

    @Test
    public void testFindById() {
        PayOrder order = getPayOrder();
        payOrderRepository.save(order);
        PayOrder result = payOrderRepository.findById(order.getId()).get();
        assertEquals(order.getId(), result.getId());
    }

    private PayOrder getPayOrder() {
        PayOrder order = new PayOrder();
        order.setOrderId("123");
        return order;
    }

}