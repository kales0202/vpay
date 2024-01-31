package com.synway.vpay.service;

import com.synway.vpay.entity.TempPrice;
import com.synway.vpay.repository.TempPriceRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Slf4j
@Service
@Validated
public class TempPriceService {

    private static final BigDecimal RATIO = new BigDecimal("0.01");

    @Resource
    private TempPriceRepository tempPriceRepository;

    public BigDecimal getRealPrice(BigDecimal price) {
        TempPrice tempPrice = new TempPrice(price);
        while (true) {
            try {
                tempPrice = tempPriceRepository.save(tempPrice);
                return tempPrice.getPrice();
            } catch (Exception e) {
                // do nothing
            }
            // realPrice + 0.01
            tempPrice.setPrice(tempPrice.getPrice().add(RATIO));
        }

    }
}
