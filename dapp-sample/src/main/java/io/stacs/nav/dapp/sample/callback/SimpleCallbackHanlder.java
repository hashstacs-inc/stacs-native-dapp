package io.stacs.nav.dapp.sample.callback;

import io.stacs.nav.dapp.core.callback.CallbackType;
import io.stacs.nav.dapp.core.callback.ITxCallbackHandler;
import io.stacs.nav.drs.api.enums.ApiConstants;
import io.stacs.nav.drs.api.model.TransactionPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author liuyu
 * @description
 * @date 2019-10-28
 */
@Component @Slf4j public class SimpleCallbackHanlder implements ITxCallbackHandler {

    @Override public CallbackType[] supportType() {
        return new CallbackType[] {CallbackType.of(ApiConstants.TransactionApiEnum.AUTHORIZE_PERMISSION.getFunctionName())};
    }

    @Override public void handle(TransactionPO po) {
        log.info("handle callback msg from block chain,po:{}", po);
    }
}
