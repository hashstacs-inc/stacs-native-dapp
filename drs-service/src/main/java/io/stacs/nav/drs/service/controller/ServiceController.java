package io.stacs.nav.drs.service.controller;

import io.stacs.nav.drs.api.model.Policy;
import io.stacs.nav.drs.api.model.RespData;
import io.stacs.nav.drs.api.model.RsDomain;
import io.stacs.nav.drs.api.model.bd.BusinessDefine;
import io.stacs.nav.drs.api.model.permission.PermissionInfoVO;
import io.stacs.nav.drs.service.event.EventPublisher;
import io.stacs.nav.drs.service.service.BlockChainService;
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
    @Autowired BlockChainService blockChainService;

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
            respData.setData(blockChainService.queryAllBDInfo(bdCode));
        } catch (Throwable e) {
            log.error("[queryBDList]has error", e);
        }
        return respData;
    }

    /**
     * query all domain info
     *
     * @return
     */
    @GetMapping("/queryAllDomain") @ResponseBody public RespData<List<RsDomain>> queryAllDomain() {
        RespData<List<RsDomain>> respData = new RespData<>();
        try {
            respData.setData(blockChainService.queryAllDomains());
        } catch (Throwable e) {
            log.error("[queryAllDomain]has error", e);
        }
        return respData;
    }

    /**
     * query all policy info
     *
     * @return
     */
    @GetMapping("/queryAllPolicy") @ResponseBody public RespData<List<Policy>> queryAllPolicy() {
        RespData<List<Policy>> respData = new RespData<>();
        try {
            respData.setData(blockChainService.queryAllPolicy());
        } catch (Throwable e) {
            log.error("[queryAllPolicy]has error", e);
        }
        return respData;
    }

    /**
     * query all policy info
     *
     * @return
     */
    @GetMapping("/queryPermissionList") @ResponseBody public RespData<List<PermissionInfoVO>> queryPermissionList() {
        RespData<List<PermissionInfoVO>> respData = new RespData<>();
        try {
            respData.setData(blockChainService.queryPermissionList());
        } catch (Throwable e) {
            log.error("[queryPermissionList]has error", e);
        }
        return respData;
    }
}
