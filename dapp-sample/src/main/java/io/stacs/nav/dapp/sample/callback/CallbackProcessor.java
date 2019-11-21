package io.stacs.nav.dapp.sample.callback;

import io.stacs.nav.dapp.core.callback.ITxCallbackHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author liuyu
 * @description
 * @date 2019-10-28
 */
@Component @Slf4j public class CallbackProcessor implements ITxCallbackHandler {
    @Override public void handle(String topic) {
        log.info("CallbackProcessor is handle topic:{},threadName:{}", topic, Thread.currentThread().getName());
    }
}
