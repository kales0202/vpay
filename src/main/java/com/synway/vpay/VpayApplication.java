package com.synway.vpay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@Slf4j
@EnableCaching
@SpringBootApplication
public class VpayApplication {

    public static void main(String[] args) {
        SpringApplication.run(VpayApplication.class, args);
    }

}
