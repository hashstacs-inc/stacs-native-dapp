package io.stacs.nav.drs.service.vo;

import io.stacs.nav.drs.api.model.callback.TransactionReceipt;
import io.stacs.nav.drs.service.model.bo.BlockHeader;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author liuyu
 * @description
 * @date 2019-11-08
 */
@Getter @Setter public class CallbackVO {
    private BlockHeader blockHeader;
    private List<TransactionReceipt> receipts;
}
