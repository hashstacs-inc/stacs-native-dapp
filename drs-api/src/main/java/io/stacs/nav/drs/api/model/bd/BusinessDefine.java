package io.stacs.nav.drs.api.model.bd;

import io.stacs.nav.drs.api.enums.FunctionDefineEnum;
import io.stacs.nav.drs.api.model.BaseTxVO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override public String getSignValue() {
        return super.getSignValue()
                    + code
                    + name
                    + bdType
                    + desc
                    + initPermission
                    + initPolicy
                    + bdVersion
                    + String.join(",",functions.stream().map(FunctionDefine::getSignValue).collect(Collectors.toList()))
                    + getFunctionName();
    }
}
