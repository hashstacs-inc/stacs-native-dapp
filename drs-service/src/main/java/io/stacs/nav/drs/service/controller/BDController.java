package io.stacs.nav.drs.service.controller;

import io.stacs.nav.drs.api.exception.DappError;
import io.stacs.nav.drs.api.exception.DappException;
import io.stacs.nav.drs.api.model.RespData;
import io.stacs.nav.drs.service.service.BDService;
import io.stacs.nav.drs.service.vo.BDVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author suimi
 * @date 2019/10/30
 */
@RestController @Slf4j @RequestMapping("/bd") public class BDController {

    @Autowired BDService bdService;

    @PostMapping("/submit") public RespData submit(@RequestBody BDVO bdvo) {
        try {
            bdService.submit(bdvo);
            return RespData.success();
        } catch (DappException e) {
            return RespData.fail(e);
        } catch (Throwable e) {
            return RespData.fail(DappError.DAPP_COMMON_ERROR);
        }
    }
}
