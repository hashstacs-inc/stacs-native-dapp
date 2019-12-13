package io.stacs.nav.drs.service.controller;

import io.stacs.nav.drs.api.exception.DappError;
import io.stacs.nav.drs.api.exception.DappException;
import io.stacs.nav.drs.api.model.RespData;
import io.stacs.nav.drs.service.service.BDService;
import io.stacs.nav.drs.service.vo.BDVO;
import io.stacs.nav.drs.service.vo.SignVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author suimi
 * @date 2019/10/30
 */
@RequestMapping("/bd") @RestController @Slf4j public class BDController {

    @Autowired BDService bdService;

    @PostMapping("/genSign") public RespData genSign(@RequestBody BDVO bdvo) {
        try {
            SignVO sign = bdService.generateSignature(bdvo);
            return RespData.success(sign);
        } catch (DappException e) {
            log.error("[genSign]has dapp error", e);
            return RespData.fail(e);
        } catch (Throwable e) {
            log.error("[genSign]has unknown error", e);
            return RespData.fail(DappError.DAPP_COMMON_ERROR);
        }
    }

    @PostMapping("/submit") public RespData submit(@RequestBody BDVO bdvo) {
        try {
            bdService.submit(bdvo);
            return RespData.success();
        } catch (DappException e) {
            log.error("[submit]has dapp error", e);
            return RespData.fail(e);
        } catch (Throwable e) {
            log.error("[submit]has unknown error", e);
            return RespData.fail(DappError.DAPP_COMMON_ERROR);
        }
    }
}
