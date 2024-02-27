package com.synway.vpay.service;

import com.synway.vpay.enums.PayType;
import com.synway.vpay.repository.TempPriceRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootTest
public class TempPriceServiceTest {

    @Resource
    private TempPriceService tempPriceService;

    @Resource
    private TempPriceRepository tempPriceRepository;

    @AfterEach
    void tearDown() {
        tempPriceRepository.deleteAll();
    }

    @Test
    public void saveReallyPrice() {
        UUID id = UUID.randomUUID();
        tempPriceService.saveReallyPrice(id, PayType.WECHAT, new BigDecimal("100"));
        tempPriceService.saveReallyPrice(id, PayType.WECHAT, new BigDecimal("100"));
        BigDecimal bigDecimal = tempPriceService.saveReallyPrice(id, PayType.WECHAT, new BigDecimal("100"));

        Assertions.assertEquals(new BigDecimal("100.02"), bigDecimal);
    }
}