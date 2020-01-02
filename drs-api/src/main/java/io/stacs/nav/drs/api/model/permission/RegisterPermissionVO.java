package io.stacs.nav.drs.api.model.permission;


import io.stacs.nav.drs.api.enums.ApiConstants;
import io.stacs.nav.drs.api.model.BaseTxVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author ganxiang
 * @date 2019/10/16
 */
@Setter @Getter @ToString(callSuper = true) public class RegisterPermissionVO extends BaseTxVO {

    @NotBlank @Length(max = 64) private String permissionName;

    @Override public String getMethodSign() {
        return ApiConstants.TransactionApiEnum.PERMISSION_REGISTER.getFunctionName();
    }

    @Override public String getFunctionName() {
        if (StringUtils.isEmpty(super.getFunctionName())) {
            return this.getMethodSign();
        }
        return super.getFunctionName();
    }
    @Override
    public String getSignValue(){
        return super.getSignValue()
                    + permissionName
                    + getFunctionName();
    }
}
