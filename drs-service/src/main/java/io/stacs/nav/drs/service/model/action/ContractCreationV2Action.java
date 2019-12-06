package io.stacs.nav.drs.service.model.action;

import io.stacs.nav.slave.model.bo.action.ActionJsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import static io.stacs.nav.slave.api.enums.ActionTypeEnum.CONTRACT_CREATION;

/**
 * The type Contract creation v 2 action.
 *
 * @author tangkun
 * @description smart contract create action v2
 * @date 2018 -11-29
 */
@ActionJsonDeserialize(types = CONTRACT_CREATION) @Getter @Setter public class ContractCreationV2Action extends Action {

    private String code;

    /**
     * tx create address
     */
    private String from;

    /**
     * contract address
     */
    private String to;

    private String name;

    private String extension;

    /**
     * method abi for contract
     */
    private String abi;

    /**
     * sourCode of contract
     */
    private String sourCode;

}
