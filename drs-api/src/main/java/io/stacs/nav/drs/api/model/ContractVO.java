package io.stacs.nav.drs.api.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Contract persist object
 *
 * @author duhongming
 * @date 2018 -04-12
 */
@Getter @Setter public class ContractVO {
    private Long id;
    private String address;
    private String name;
    private String symbol;
    private String extension;
    private String bdCode;
    private String status;
    private Long blockHeight;
    private String txId;
    private Integer actionIndex;
    private String version;
    private String code;
    private Long createTime;
    private String bdType;
}
