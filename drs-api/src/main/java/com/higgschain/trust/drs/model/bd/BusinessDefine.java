package com.higgschain.trust.drs.model.bd;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @author dekuofa <br>
 * @date 2019-11-04 <br>
 */
@Getter @Setter public class BusinessDefine {

    private Long id;

    private String code;

    private String name;

    private String bdType;

    private String desc;

    private String initPermission;

    private String initPolicy;

    private List<FunctionDefine> functions;

    private String version;

    private Date createTime;

    private Date updateTime;
}
