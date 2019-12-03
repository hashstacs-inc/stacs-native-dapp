package io.stacs.nav.drs.api.model.query;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * The type Query tx vo.
 *
 * @author liuyu
 * @description
 * @date 2018 -07-25
 */
@Getter @Setter public class QueryTxVO {
    @NotNull @Size(max = 64) private String txId;
}
