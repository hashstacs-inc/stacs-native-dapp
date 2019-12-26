package io.stacs.nav.dapp.core.event;

import com.alibaba.fastjson.JSON;
import com.alipay.sofa.ark.spi.event.ArkEvent;
import com.alipay.sofa.ark.spi.service.event.EventHandler;
import io.stacs.nav.dapp.core.callback.CallbackProcessor;
import io.stacs.nav.drs.api.event.DappEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author suimi
 * @date 2019/10/30
 */
@Slf4j @Component public class DappEventHandler implements EventHandler {
    @Autowired ThreadPoolExecutor callbackThreadPool;
    @Autowired CallbackProcessor callbackProcessor;

    @Override public void handleEvent(ArkEvent event) {
        log.info("received dapp event:{}", JSON.toJSONString(event));
        if (event instanceof DappEvent) {
            DappEvent dappEvent = (DappEvent)event;
            log.info("process dapp event:{}", dappEvent);
            callbackThreadPool.execute(() -> callbackProcessor.process(dappEvent.getValue()));
        }
    }

    @Override public int getPriority() {
        return 0;
    }
}
