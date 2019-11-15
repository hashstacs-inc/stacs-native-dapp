package com.higgschain.trust.drs.service.model;

import com.higgschain.trust.drs.service.enums.RequestStatus;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data public class TxRequestBO {
    /**
     * the transaction id
     */
    @NotBlank @Length(max = 64) private String txId;
    /**
     * policy Id
     */
    @NotBlank @Length(max = 32) private String policyId;
    /**
     * tx submitter
     */
    @NotBlank @Length(max = 40) private String submitter;
    /**
     * business code
     */
    @NotBlank @Length(max = 32) private String bdCode;
    /**
     * function name
     */
    @NotBlank @Length(max = 128) private String funcName;
    /**
     * the request data
     */
    @NotNull private Object txData;
    /**
     * status,INIT、SUBMITTING、PROCESSING、END
     */
    private RequestStatus status;
}
