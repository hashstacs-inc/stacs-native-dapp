package com.higgschain.trust.drs.service.controller;

import com.higgschain.trust.drs.service.event.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author suimi
 * @date 2019/10/30
 */
@RestController @Slf4j @RequestMapping("/drs") public class ServiceController {

    @Autowired EventPublisher eventPublisher;

    /**
     * publish event
     *
     * @return
     */
    @GetMapping("/publish/{height}/{txId}/{value}") public void publish(@PathVariable("height") long height,
        @PathVariable("txId") String txId, @PathVariable("value") String value) {
        eventPublisher.publish(height, txId, value);
    }
}
