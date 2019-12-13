package io.stacs.nav.drs.service.controller;

import io.stacs.nav.crypto.StacsECKey;
import io.stacs.nav.drs.api.exception.DappError;
import io.stacs.nav.drs.api.exception.DappException;
import io.stacs.nav.drs.api.model.RespData;
import io.stacs.nav.drs.service.service.BDService;
import io.stacs.nav.drs.service.vo.BDVO;
import io.stacs.nav.drs.service.vo.GenSignVO;
import io.stacs.nav.drs.service.vo.SignVO;
import lombok.extern.slf4j.Slf4j;
import org.spongycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author suimi
 * @date 2019/10/30
 */
@RequestMapping("/bd") @RestController @Slf4j public class BDController {

    @Autowired BDService bdService;

    @PostMapping("/sign") public RespData sign(@RequestBody GenSignVO vo) {
        try {
            SignVO signVO = bdService.getSignValue(vo.getBdvo());
            String s = StacsECKey.fromPrivate(Hex.decode(vo.getPriKey())).signMessage(signVO.getSign());
            return RespData.success(s);
        } catch (DappException e) {
            log.error("[sign]has dapp error", e);
            return RespData.fail(e);
        } catch (Throwable e) {
            log.error("[sign]has unknown error", e);
            return RespData.fail(DappError.DAPP_COMMON_ERROR);
        }
    }


    @PostMapping("/getSignValue") public RespData genSign(@RequestBody BDVO bdvo) {
        try {
            SignVO sign = bdService.getSignValue(bdvo);
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
