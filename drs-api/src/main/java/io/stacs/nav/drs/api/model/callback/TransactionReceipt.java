package io.stacs.nav.drs.api.model.callback;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The type Transaction receipt.
 *
 * @author liuyu
 * @description the execution result of the transaction
 * @date 2018 -04-09
 */
@Getter @Setter @ToString(callSuper = true) public class TransactionReceipt {
    /**
     * the id of the transaction
     */
    private String txId;
    /**
     * the execution result of the transaction
     */
    private boolean result;
    /**
     * error code for transaction execution
     */
    private String errorCode;

    /**
     * error message for transaction execution
     */
    private String errorMessage;

    /**
     * the transaction execute result data
     */
    private TxReceiptData receiptData;

    /**
     * transaction version
     */
    private String version;
}
