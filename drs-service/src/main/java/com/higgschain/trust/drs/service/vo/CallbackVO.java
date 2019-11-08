package com.higgschain.trust.drs.service.vo;

import com.higgschain.trust.drs.model.callback.TransactionReceipt;
import com.higgschain.trust.drs.service.model.block.BlockHeader;
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
