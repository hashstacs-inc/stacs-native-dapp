package io.stacs.nav.drs.api.model.bd;

import io.stacs.nav.drs.api.enums.ApiConstants;
import io.stacs.nav.drs.api.model.BaseTxVO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dekuofa <br>
 * @date 2019-11-04 <br>
 */
@Getter @Setter public class BusinessDefine extends BaseTxVO {

    private String code;

    private String name;
    @Length(max = 32)
    private String bdType;

    private String desc;

    private String initPermission;

    private String initPolicy;

    private List<FunctionDefine> functions;

    private String bdVersion;

    @Override public String getMethodSign() {
        return ApiConstants.TransactionApiEnum.BD_PUBLISH.getFunctionName();
    }

    @Override public String getFunctionName() {
        if (StringUtils.isEmpty(super.getFunctionName())) {
            return this.getMethodSign();
        }
        return super.getFunctionName();
    }

    @Override public String getSignValue() {
        String signValue = super.getSignValue();
        signValue = signValue + getCode() + getName() + getBdType() + getDesc() + getInitPermission() + getInitPolicy()
            + getBdVersion() + String
            .join(",", getFunctions().stream().map(FunctionDefine::getSignValue).collect(Collectors.toList()))
            + getFunctionName();
        return signValue;
    }
}
