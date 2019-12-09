package io.stacs.nav.drs.api.model.action;

import io.stacs.nav.drs.api.model.Action;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Contract creation v 2 action.
 *
 * @author tangkun
 * @description smart contract create action v2
 * @date 2018 -11-29
 */
@Getter @Setter public class ContractCreationV2Action extends Action {

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
