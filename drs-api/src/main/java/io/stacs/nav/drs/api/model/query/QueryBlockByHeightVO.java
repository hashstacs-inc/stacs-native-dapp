package io.stacs.nav.drs.api.model.query;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * The type Query block by height vo.
 *
 * @author liuyu
 * @description
 * @date 2018 -07-25
 */
@Getter @Setter public class QueryBlockByHeightVO {
    @NotNull private Long height;
}
