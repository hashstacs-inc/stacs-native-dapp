package com.higgschain.trust.drs.api.model.bd;

import com.higgschain.trust.drs.api.enums.FunctionDefineEnum;
import com.higgschain.trust.drs.api.model.BaseTxVO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author dekuofa <br>
 * @date 2019-11-04 <br>
 */
@Getter @Setter public class BusinessDefine extends BaseTxVO {

    private String code;

    private String name;

    private String bdType;

    private String desc;

    private String initPermission;

    private String initPolicy;

    private List<FunctionDefine> functions;

    private String bdVersion;

    @Override public String getFunctionName() {
        return FunctionDefineEnum.BD_PUBLISH.getFunctionName();
    }
}
