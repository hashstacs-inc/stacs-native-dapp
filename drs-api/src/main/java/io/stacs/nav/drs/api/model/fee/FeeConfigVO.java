package io.stacs.nav.drs.api.model.fee;

import io.stacs.nav.drs.api.model.BaseTxVO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

import static io.stacs.nav.drs.api.enums.ApiConstants.TransactionApiEnum.SET_FEE_CONFIG;

/**
 * @author liuyu
 * @description
 * @date 2019-09-09
 */
@Getter @Setter public class FeeConfigVO extends BaseTxVO {
    /**
     * the contract address of fee
     */
    @NotNull private String contractAddress;
    /**
     * the received address of fee
     */
    @NotNull private String receiveAddr;

    @Override public String getFunctionName() {
        return SET_FEE_CONFIG.getFunctionName();
    }
}
