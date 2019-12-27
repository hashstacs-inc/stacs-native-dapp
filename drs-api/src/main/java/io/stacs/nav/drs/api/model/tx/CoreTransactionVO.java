package io.stacs.nav.drs.api.model.tx;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * The type Core transaction vo.
 *
 * @author tangfashuang
 * @date 2018 /05/12 15:18
 * @desc
 */
@Setter @Getter public class CoreTransactionVO {
    /**
     * transaction id
     */
    private String txId;
    /**
     * the save data of the core
     */
    private Object bizModel;
    /**
     * policy id
     */
    private String policyId;
    /**
     * the version of the tx
     */
    private String version;
    /**
     * the block height of the tx
     */
    private Long blockHeight;
    /**
     * the create time of the block for the tx
     */
    private Date blockTime;
    /**
     * the tx sender's rsId
     */
    private String sender;
    /**
     * action list
     */
    private String actionDatas;
    /**
     * the tx execute result 0:fail 1:success
     */
    private String executeResult;
    /**
     * execute error code
     */
    private String errorCode;
    /**
     * execute message code
     */
    private String errorMessage;
    /**
     * the type of transaction
     */
    private String functionName;
    /**
     * contract state
     */
    private Object contractState;
    /**
     * transaction bd code
     */
    private String bdCode;
    /**
     * transaction submitter
     */
    private String submitter;
    /**
     * signature of submitter
     */
    private String submitterSign;
}
