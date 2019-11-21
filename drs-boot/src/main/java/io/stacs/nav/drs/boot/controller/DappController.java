package io.stacs.nav.drs.boot.controller;

import com.alipay.sofa.ark.spi.model.Biz;
import com.alipay.sofa.ark.spi.model.BizState;
import com.alipay.sofa.ark.spi.service.ArkInject;
import com.alipay.sofa.ark.spi.service.biz.BizManagerService;
import io.stacs.nav.drs.boot.bo.Dapp;
import io.stacs.nav.drs.boot.enums.DappStatus;
import io.stacs.nav.drs.api.exception.DappError;
import io.stacs.nav.drs.api.exception.DappException;
import io.stacs.nav.drs.boot.service.IDappLifecycleManage;
import io.stacs.nav.drs.boot.service.dapp.IDappService;
import io.stacs.nav.drs.api.model.RespData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping("/download") public RespData download(@RequestParam String filePath) throws IOException {
        log.info("start download dapp by filePath:{}", filePath);
        try {
            return RespData.success(dappLifecycleManage.download(filePath));
        } catch (DappException e) {
            log.error("[download]has dapp error", e);
            return RespData.fail(e.getCode(), e.getMsg());
        } catch (Throwable e) {
            log.error("[download]has unknown error", e);
            return RespData.fail(DappError.DAPP_COMMON_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * initialized dapp
     *
     * @return
     */
    @GetMapping("/init/{appName}") public RespData initialized(@PathVariable String appName) {
        try {
            return RespData.success(dappLifecycleManage.initialized(appName));
        } catch (DappException e) {
            log.error("[initialized]has dapp error", e);
            return RespData.fail(e.getCode(), e.getMsg());
        } catch (Throwable e) {
            log.error("[initialized]has unknown error", e);
            return RespData.fail(DappError.DAPP_COMMON_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * install dapp
     *
     * @return
     */
    @GetMapping("/install/{appName}") public RespData install(@PathVariable String appName) {
        log.info("start install dapp by appName:{}", appName);
        try {
            return RespData.success(dappLifecycleManage.install(appName));
        } catch (DappException e) {
            log.error("[install]has dapp error", e);
            return RespData.fail(e.getCode(), e.getMsg());
        } catch (Throwable e) {
            log.error("[install]has unknown error", e);
            return RespData.fail(DappError.DAPP_COMMON_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * uninstall dapp
     *
     * @return
     */
    @GetMapping("/uninstall/{appName}") public RespData uninstall(@PathVariable("appName") String appName) {
        try {
            return RespData.success(dappLifecycleManage.unInstall(appName));
        } catch (DappException e) {
            log.error("[uninstall]has dapp error", e);
            return RespData.fail(e.getCode(), e.getMsg());
        } catch (Throwable e) {
            log.error("[uninstall]has unknown error", e);
            return RespData.fail(DappError.DAPP_COMMON_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * show all dapp
     *
     * @return
     */
    @GetMapping("/installList") public RespData installList() {
        return RespData.success(dappLifecycleManage.list());
    }

    /**
     * query dapp config
     *
     * @return
     */
    @GetMapping("/config/query/{appName}") public RespData queryConfig(@PathVariable(name = "appName") String appName) {
        log.info("queryConfig appName :{} ", appName);
        try {
            return RespData.success(dappLifecycleManage.queryConfig(appName));
        } catch (DappException e) {
            log.error("[queryConfig]has dapp error", e);
            return RespData.fail(e.getCode(), e.getMsg());
        } catch (Throwable e) {
            log.error("[queryConfig]has unknown error", e);
            return RespData.fail(DappError.DAPP_COMMON_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * config dapp
     *
     * @return
     */
    @PostMapping("/config/{appName}") public RespData config(@PathVariable(name = "appName") String appName,
        @RequestBody Map<String, String> params) {
        log.info("config dapp :{} with params:{}", appName, params);
        Map<String, String> map = new HashMap<>();
        map.putAll(params);
        try {
            dappLifecycleManage.config(appName, map);
            return RespData.success();
        } catch (DappException e) {
            log.error("[config]has dapp error", e);
            return RespData.fail(e.getCode(), e.getMsg());
        } catch (Throwable e) {
            log.error("[config]has unknown error", e);
            return RespData.fail(DappError.DAPP_COMMON_ERROR.getCode(), e.getMessage());
        }
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
            return DappStatus.RUNNING;
        }
        return DappStatus.STOPPED;
    }

}
