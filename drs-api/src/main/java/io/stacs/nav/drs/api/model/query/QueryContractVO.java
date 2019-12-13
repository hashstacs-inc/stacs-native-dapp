package io.stacs.nav.drs.api.model.query;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * The type Query tx vo.
 *
 * @author liuyu
 * @description
 * @date 2018 -07-25
 */
@Getter @Setter public class QueryContractVO {
    private String txId;
    @NotEmpty private String bdType;
    private String name;
    private String symbol;
    private String status;
}
