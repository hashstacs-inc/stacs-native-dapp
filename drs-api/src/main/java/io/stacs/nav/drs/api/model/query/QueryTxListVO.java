package io.stacs.nav.drs.api.model.query;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * The type Query transaction vo.
 *
 * @author tangfashuang
 * @date 2018 /05/12 18:38
 * @desc query transaction request
 */
@Getter @Setter public class QueryTxListVO {

    private Long blockHeight;

    @Size(max = 64) private String txId;

    @Size(max = 32) private String submitter;

    @NotNull private Integer pageNum;
    @NotNull private Integer pageSize;

    private String order = "desc";
}
