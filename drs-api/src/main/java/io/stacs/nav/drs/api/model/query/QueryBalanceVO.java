package io.stacs.nav.drs.api.model.query;

import lombok.Getter;
import lombok.Setter;

/**
 * The type Query tx vo.
 *
 * @author liuyu
 * @description
 * @date 2018 -07-25
 */
@Getter @Setter public class QueryBalanceVO {
    private String contract;
    private String identity;
    private Long height;
}
