package io.stacs.nav.dapp.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author liuyu <br>
 * @date 2019-12-16 <br>
 */
@Configuration public class ThreadPoolConfig {

    @Bean("callbackThreadPool") public ThreadPoolExecutor callbackThreadPool() {
        return new ThreadPoolExecutor(1, 16, 5, SECONDS, new LinkedBlockingQueue<>(10));
    }
}
