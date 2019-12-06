package io.stacs.nav.drs.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author dekuofa <br>
 * @date 2019-12-05 <br>
 */
@Configuration public class ThreadPoolConfig {

    @Bean("syncBlockThreadPool") public ThreadPoolExecutor syncBlockThreadPool() {
        return new ThreadPoolExecutor(1, 16, 5, SECONDS, new LinkedBlockingQueue<>(1));
    }
}
