package io.stacs.nav.drs.api.model.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Contract query request v 2.
 *
 * @author Chen Jiawei
 * @date 2018 -12-12
 */
@Data @AllArgsConstructor @NoArgsConstructor public class ContractQueryRequest {
    /**
     * Height of block.
     */
    private Long blockHeight;
    /**
     * Contract address, hex string of 40 characters, e.g. 00a615668486da40f31fd050854fb137b317e056.
     */
    private String address;
    /**
     * 余额查询 ：uint256） balanceOf(address)
     * Method signature, e.g. (uint256) get(uint256).
     */
    private String methodSignature;
    /**
     * Method input arguments, e.g. 4.
     */
    private Object[] parameters;
}
