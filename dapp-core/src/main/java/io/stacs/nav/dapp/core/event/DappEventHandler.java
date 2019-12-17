package io.stacs.nav.dapp.core.event;

import com.alipay.sofa.ark.spi.event.ArkEvent;
import com.alipay.sofa.ark.spi.service.event.EventHandler;
import io.stacs.nav.dapp.core.callback.ITxCallbackHandler;
import io.stacs.nav.drs.api.event.DappEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author suimi
 * @date 2019/10/30
 */
@Slf4j @Component public class DappEventHandler implements EventHandler, ApplicationContextAware {
    @Autowired ThreadPoolExecutor callbackThreadPool;
    @Autowired ApplicationContext applicationContext;

    @Override public void handleEvent(ArkEvent event) {
        log.info("received dapp event:{}", event);
        if (event instanceof DappEvent) {
            DappEvent dappEvent = (DappEvent)event;
            log.info("process dapp event:{}", dappEvent);
            ITxCallbackHandler txCallbackHandler = applicationContext.getBean(ITxCallbackHandler.class);
            if (txCallbackHandler != null) {
                callbackThreadPool.execute(() -> txCallbackHandler.handle(dappEvent.getValue()));
            }
        }
    }

    @Override public int getPriority() {
        return 0;
    }

    @Override public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
