package io.stacs.nav.dapp.explorer.controller;

import io.stacs.nav.drs.api.model.RespData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dekuofa <br>
 * @date 2019-12-02 <br>
 */
@RestController @RequestMapping("/block") @Slf4j public class BlockController {

    public RespData<?> queryCurrentHeight() {
        return null;
    }
}
