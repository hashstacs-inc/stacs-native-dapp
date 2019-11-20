package com.higgschain.trust.dapp.sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.Arrays;

@SpringBootApplication(scanBasePackages = {"com.higgschain.trust.dapp"}) @Slf4j
@EnableAspectJAutoProxy public class SampleApplication {

    public static void main(String[] args) {
        log.info("starting dapp with args:{}", Arrays.toString(args));
        SpringApplication.run(SampleApplication.class, args);
        log.info("demo is running");
    }
}
