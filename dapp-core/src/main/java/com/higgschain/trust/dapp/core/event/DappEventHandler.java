package com.higgschain.trust.dapp.core.event;

import com.alipay.sofa.ark.spi.event.ArkEvent;
import com.alipay.sofa.ark.spi.service.event.EventHandler;
import com.higgschain.trust.drs.event.DappEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author suimi
 * @date 2019/10/30
 */
@Slf4j @Component public class DappEventHandler implements EventHandler {
    
    @Override public void handleEvent(ArkEvent event) {
        log.info("received dapp event:{}", event);
        if (event instanceof DappEvent) {
            DappEvent dappEvent = (DappEvent)event;
            log.info("process dapp event:{}", dappEvent);
        }
    }

    @Override public int getPriority() {
        return 0;
    }
}
