package io.stacs.nav.drs.service.dao.po;

import com.alibaba.fastjson.annotation.JSONField;
import io.stacs.nav.drs.service.dao.ActionPO;
import lombok.Getter;
import lombok.Setter;

/**
 * Contract persist object
 *
 * @author duhongming
 * @date 2018 -04-12
 */
@Getter @Setter public class ContractPO implements ActionPO {
    private Long id;
    @JSONField(name = "to") private String address;
    private String name;
    private String symbol;
    private String extension;
    private String bdCode;
    private String status;
    private Long blockHeight;
    private String txId;
    @JSONField(name = "index") private Integer actionIndex;
    private String version;
    @JSONField(name = "sourCode") private String code;
    private Long createTime;
}
