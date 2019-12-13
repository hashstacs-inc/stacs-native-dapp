package io.stacs.nav.drs.service.dao.po;

import io.stacs.nav.drs.service.dao.ActionPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author dekuofa <br>
 * @date 2019-11-04 <br>
 */
@ToString @Getter @Setter public class BusinessDefinePO implements ActionPO {

    private Long id;

    private String code;

    private String name;

    private String bdType;

    private String desc;

    private String initPermission;

    private String initPolicy;

    private String functions;

    private String bdVersion;

    private Long createTime;

    private Long updateTime;

}
