package io.stacs.nav.dapp.sample.service;

import com.alipay.sofa.ark.spi.service.ArkInject;
import io.stacs.nav.drs.api.IDappApiService;
import io.stacs.nav.drs.api.exception.DappException;
import io.stacs.nav.drs.api.model.RespData;
import io.stacs.nav.drs.api.model.permission.AuthPermissionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static io.stacs.nav.drs.api.model.RespData.fail;
import static io.stacs.nav.drs.api.model.RespData.success;

/**
 * @author suimi
 * @date 2019/10/30
 */
@Service @Slf4j public class SampleService {

    @ArkInject IDappApiService dappService;

    public RespData<?> authPermission(AuthPermissionVO vo) {
        log.info("dapp service is called...");
        try {
            dappService.authPermission(vo);
            return success();
        } catch (DappException e) {
            log.error("has error", e);
            return fail(e);
        }
    }
}
