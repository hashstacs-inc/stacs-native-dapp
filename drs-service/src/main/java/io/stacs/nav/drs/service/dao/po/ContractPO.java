package io.stacs.nav.drs.service.dao.po;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Contract persist object
 *
 * @author duhongming
 * @date 2018 -04-12
 */
@Getter @Setter public class ContractPO {
    private Long id;
    private String address;
    private String name;
    private String extension;
    private String bdCode;
    private String bdCodeVersion;
    private String status;
    private Long blockHeight;
    private String txId;
    private Integer actionIndex;
    private String language;
    private String version;
    private String code;
    private Date createTime;
}
