package com.higgschain.trust.dapp.sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages = {"com.higgschain.trust.drs", "com.higgschain.trust.dapp"}) @Slf4j
@EnableAspectJAutoProxy public class SampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
        log.info("demo is running");
    }
}
