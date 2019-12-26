package io.stacs.nav.drs.service.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liuyu
 * @description
 * @date 2019-12-13
 */
@Getter @Setter @ToString public class GetSignVO {
    private String priKey;
    private String signValue;
}