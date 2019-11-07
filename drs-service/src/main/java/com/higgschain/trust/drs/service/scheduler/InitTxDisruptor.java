package com.higgschain.trust.drs.service.scheduler;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.higgschain.trust.drs.service.model.TxRequestBO;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @author suimi
 * @date 2019/8/15
 */
@Component @Slf4j public class InitTxDisruptor {

    @Value("${drs.disruptor.thread.size:2}") private int initSize;

    @Autowired private InitTxWorkHandler initTxWorkHandler;

    private Disruptor<InitTxEvent> initDisruptor;

    private static final Cache<String, String> INIT_PROCESS_CACHE =
        CacheBuilder.newBuilder().initialCapacity(100).maximumSize(10000).expireAfterWrite(60, TimeUnit.SECONDS)
            .build();

    @PostConstruct public void init() {
        this.initDisruptor = DisruptorBuilder.<InitTxEvent>newInstance().setRingBufferSize(64 * 1024)
            .setEventFactory(() -> new InitTxEvent())
            .setThreadFactory(new ThreadFactoryBuilder().setNameFormat("Init-TX-Disruptor-%d").build())
            .setProducerType(ProducerType.MULTI).setWaitStrategy(new BlockingWaitStrategy()).build();
        this.initDisruptor.setDefaultExceptionHandler(new LogExceptionHandler<>(getClass().getSimpleName()));

        InitTxWorkHandler[] handlers = new InitTxWorkHandler[initSize];
        for (int i = 0; i < initSize; i++) {
            handlers[i] = initTxWorkHandler;
        }
        this.initDisruptor.handleEventsWithWorkerPool(handlers);
        this.initDisruptor.start();
        log.info("init Disruptor is end,task.size:{}", initSize);
    }

    /**
     * publish
     * @param txId
     * @param bo
     */
    public void publish(String txId, TxRequestBO bo) {
        if (INIT_PROCESS_CACHE.getIfPresent(txId) != null) {
            log.warn("already published txId:{}", txId);
            return;
        }
        INIT_PROCESS_CACHE.put(txId, txId);
        RingBuffer<InitTxEvent> initQueue = this.initDisruptor.getRingBuffer();
        initQueue.publishEvent((event, sequence) -> event.set(txId, bo));
    }
}
