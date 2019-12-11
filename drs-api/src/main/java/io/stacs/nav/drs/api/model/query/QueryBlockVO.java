package io.stacs.nav.drs.api.model.query;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * The type Query block vo.
 *
 * @author tangfashuang
 */
@Getter @Setter public class QueryBlockVO {

    private Long height;

    @Size(max = 64) private String blockHash;

    @NotNull private Integer pageNo = 1;

    @NotNull private Integer pageSize = 20;

}
