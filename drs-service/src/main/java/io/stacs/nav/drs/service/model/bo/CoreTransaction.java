package io.stacs.nav.drs.service.model.bo;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static io.stacs.nav.drs.service.enums.TxTypeEnum.DEFAULT;

/**
 * The type Core transaction.
 *
 * @author WangQuanzhou
 * @desc core transaction class
 * @date 2018 /3/26 16:59
 */
@Getter @Setter public class CoreTransaction implements BaseBO {

    private static final long serialVersionUID = -8957262691789681491L;

    /**
     * transaction id
     */
    @Length(max = 64) private String txId;

    /**
     * policy id
     */
    @Length(max = 32) private String policyId;

    /**
     * the list that store actions
     */
    @Valid private String actionList;

    /**
     * the JSONObject that store data which will be packaged into blockChain .It  can be null , when there is no need for it
     */
    private JSONObject bizModel;

    /**
     * lock time
     */
    private Date lockTime;

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

    /**
     * transaction send time
     */
    @NotNull private Date sendTime;
    /**
     * the tx sender's rsId
     */
    private String sender;
    /**
     * transaction version
     */
    private String version;
    /**
     * the type of transaction
     */
    private String txType = DEFAULT.getCode();
    /**
     * policy version of tx
     */
    private int policyVersion;
    /**
     * The number of deals
     */
    private int dealCount = 1;
    /**
     * maximum fee allowed
     */
    private String maxAllowFee;
    /**
     * address of fee payment
     */
    private String feePaymentAddress;
    /**
     * fee currency for use
     */
    private String feeCurrency;
}
