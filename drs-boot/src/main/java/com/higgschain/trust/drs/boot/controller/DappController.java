package com.higgschain.trust.drs.boot.controller;

import com.alipay.sofa.ark.spi.model.Biz;
import com.alipay.sofa.ark.spi.model.BizState;
import com.alipay.sofa.ark.spi.service.ArkInject;
import com.alipay.sofa.ark.spi.service.biz.BizManagerService;
import com.higgschain.trust.drs.boot.service.dapp.Dapp;
import com.higgschain.trust.drs.boot.service.dapp.IDappService;
import com.higgschain.trust.drs.boot.service.IDappLifecycleManage;
import com.higgschain.trust.drs.boot.service.dapp.DappStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author suimi
 * @date 2019/10/30
 */
@RestController @Slf4j @RequestMapping("/dapp") public class DappController {

    @Autowired IDappLifecycleManage dappLifecycleManage;

    @ArkInject BizManagerService bizManagerService;

    @Autowired IDappService dappService;

    /**
     * install dapp
     *
     * @return
     */
    @GetMapping("/download") public Dapp download(@RequestParam String filePath) throws IOException {
        log.info("start download dapp by filePath:{}", filePath);
        return dappLifecycleManage.download(filePath);
    }

    /**
     * install dapp
     *
     * @return
     */
    @GetMapping("/config/{dappId}") public void config(@PathVariable(name = "dappId") Long dappId,
        @RequestParam Map<String, String> params) {
        log.info("config dapp :{} with params:{}", dappId, params);
        Map<String, String> map = new HashMap<>();
        map.putAll(params);
        dappLifecycleManage.config(dappId, map);
    }

    /**
     * install dapp
     *
     * @return
     */
    @GetMapping("/install/{fileName}") public boolean install(@PathVariable String fileName) {
        log.info("start install dapp by filePath:{}", fileName);
        return dappLifecycleManage.install(fileName);
    }

    /**
     * install dapp
     *
     * @return
     */
    @GetMapping("/installList") public List<Dapp> installList() {
        return dappLifecycleManage.list();
    }

    /**
     * actual install dapp
     *
     * @return
     */
    @GetMapping("/actualList") public List<Dapp> actualList() {
        List<Dapp> dapps = new ArrayList<>();
        Set<String> allBizNames = bizManagerService.getAllBizNames();
        for (String name : allBizNames) {
            List<Biz> bizList = bizManagerService.getBiz(name);
            List<Dapp> collect = bizList.stream().map(biz -> {
                Dapp dapp = new Dapp();
                dapp.setName(biz.getBizName());
                dapp.setVersion(biz.getBizVersion());
                dapp.setStatus(map(biz.getBizState()));
                dapp.setContextPath(biz.getWebContextPath());
                return dapp;
            }).collect(Collectors.toList());
            dapps.addAll(collect);
        }
        return dapps;
    }

    private DappStatus map(BizState bizState) {
        if (BizState.ACTIVATED == bizState) {
            return DappStatus.Installed;
        }
        return null;
    }

    /**
     * uninstall dapp
     *
     * @return
     */
    @GetMapping("/uninstall/{serviceName}/{version}") public boolean uninstall(
        @PathVariable("serviceName") String dappName, @PathVariable("version") String version) {
        return dappLifecycleManage.unInstall(dappName, version);
    }

}
