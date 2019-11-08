package com.higgschain.trust.drs.model.callback;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author liuyu
 * @description
 * @date 2019-09-04
 */
@Getter
@Setter
public class FeeInfo {
    /**
     * collected fee amount of transaction
     */
    private BigDecimal feeAmount = BigDecimal.ZERO;
    /**
     * fee ratio
     */
    private BigDecimal feeRatio = BigDecimal.ZERO;

}
