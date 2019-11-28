package io.stacs.nav.drs.service.controller;

import io.stacs.nav.drs.api.model.RespData;
import io.stacs.nav.drs.api.model.bd.BusinessDefine;
import io.stacs.nav.drs.service.event.EventPublisher;
import io.stacs.nav.drs.service.service.BDService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author suimi
 * @date 2019/10/30
 */
@RestController @Slf4j @RequestMapping("/drs") public class ServiceController {

    @Autowired EventPublisher eventPublisher;
    @Autowired BDService bdService;

    /**
     * publish event
     *
     * @return
     */
    @GetMapping("/publish/{height}/{txId}/{value}") public void publish(@PathVariable("height") long height,
        @PathVariable("txId") String txId, @PathVariable("value") String value) {
        eventPublisher.publish(height, txId, null);
    }

    /**
     * query BD list info
     *
     * @return
     */
    @GetMapping("/bd/query") @ResponseBody public RespData<List<BusinessDefine>> queryBDList(
        @RequestParam(required = false) String bdCode) {
        RespData<List<BusinessDefine>> respData = new RespData<>();
        try {
            respData.setData(bdService.queryAllBDInfo(bdCode));
        } catch (Throwable e) {
            log.error("[queryBDList]has error", e);
        }
        return respData;
    }
}
