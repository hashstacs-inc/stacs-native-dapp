package io.stacs.nav.dapp.core.event;

import com.alipay.sofa.ark.spi.event.ArkEvent;
import com.alipay.sofa.ark.spi.event.BizEvent;
import com.alipay.sofa.ark.spi.model.Biz;
import com.alipay.sofa.ark.spi.service.event.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author suimi
 * @date 2019/10/30
 */
@Component @Slf4j public class DappLifecycleEventHandler implements EventHandler {

    @Value("${spring.application.name}") String name;

    /**
     * start-event for biz
     */
    private final static String START_EVENT_TOPIC = "AFTER-INVOKE-BIZ-START";
    /**
     * stop-event for biz
     */
    private final static String STOP_EVENT_TOPIC = "AFTER-INVOKE-BIZ-STOP";
    /**
     * context of spring
     */
    @Autowired private ConfigurableApplicationContext context;

    @Override public void handleEvent(ArkEvent event) {
        log.info("received event:{}", event);
        if (event instanceof BizEvent) {
            BizEvent bizEvent = (BizEvent)event;
            Biz biz = bizEvent.getBiz();
            if (biz.getBizName().equalsIgnoreCase(name) && STOP_EVENT_TOPIC.equals(event.getTopic())) {
                log.info("close dapp[{}] context", name);
                context.close();
                return;
            }
        }
    }

    @Override public int getPriority() {
        return 0;
    }

}
