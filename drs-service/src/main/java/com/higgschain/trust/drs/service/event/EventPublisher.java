package com.higgschain.trust.drs.service.event;

import com.alipay.sofa.ark.spi.service.ArkInject;
import com.alipay.sofa.ark.spi.service.event.EventAdminService;
import com.higgschain.trust.drs.api.event.DappEvent;
import com.higgschain.trust.drs.api.model.callback.TransactionReceipt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author suimi
 * @date 2019/10/30
 */
@Component @Slf4j public class EventPublisher {

    @ArkInject private EventAdminService eventAdminService;

    public void publish(long height, String txId, TransactionReceipt value) {
        DappEvent event = new DappEvent();
        event.setHeight(height);
        event.setTxId(txId);
        event.setValue(value);
        eventAdminService.sendEvent(event);
    }
}
