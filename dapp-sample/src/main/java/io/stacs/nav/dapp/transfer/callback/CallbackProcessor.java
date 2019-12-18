package io.stacs.nav.dapp.transfer.callback;

import io.stacs.nav.dapp.core.callback.ITxCallbackHandler;
import io.stacs.nav.drs.api.enums.ActionTypeEnum;
import io.stacs.nav.drs.api.model.TransactionPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author liuyu
 * @description
 * @date 2019-10-28
 */
@Component @Slf4j public class CallbackProcessor implements ITxCallbackHandler {

    @Override public ActionTypeEnum supportType() {
        return ActionTypeEnum.REGISTER_POLICY;
    }

    @Override public void handle(TransactionPO po) {
        log.info("handle callback msg from block chain,po:{}", po);
    }
}
