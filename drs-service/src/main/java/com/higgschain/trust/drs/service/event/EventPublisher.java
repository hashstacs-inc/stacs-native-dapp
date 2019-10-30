package com.higgschain.trust.drs.service.event;

import com.alipay.sofa.ark.spi.service.ArkInject;
import com.alipay.sofa.ark.spi.service.event.EventAdminService;
import com.higgschain.trust.drs.event.DappEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author suimi
 * @date 2019/10/30
 */
@Component @Slf4j public class EventPublisher {

    @ArkInject private EventAdminService eventAdminService;

    public void publish(long height, String txId, String value) {
        DappEvent event = new DappEvent();
        event.setHeight(height);
        event.setTxId(txId);
        event.setValue(value);
        eventAdminService.sendEvent(event);
    }
}
