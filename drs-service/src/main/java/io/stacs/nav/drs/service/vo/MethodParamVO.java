package io.stacs.nav.drs.service.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyu
 * @description
 * @date 2019-12-16
 */
@Getter
@Setter
public class MethodParamVO {
    /**
     * address of contract
     */
    private String contractAddress;

    /**
     * transfer(address,uint256)
     */
    private String methodSign;
}
