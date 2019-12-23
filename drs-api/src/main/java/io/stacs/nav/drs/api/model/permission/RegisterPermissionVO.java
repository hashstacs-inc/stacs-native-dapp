package io.stacs.nav.drs.api.model.permission;


import io.stacs.nav.drs.api.model.BaseTxVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import static io.stacs.nav.drs.api.enums.ApiConstants.TransactionApiEnum.PERMISSION_REGISTER;

/**
 * @author ganxiang
 * @date 2019/10/16
 */
@Setter @Getter @ToString(callSuper = true) public class RegisterPermissionVO extends BaseTxVO {

    @NotBlank @Length(max = 64) private String permissionName;

    @Override public String getFunctionName() {
        return PERMISSION_REGISTER.getFunctionName();
    }

    @Override
    public String getSignValue(){
        return super.getSignValue()
                    + permissionName
                    + getFunctionName();
    }
}
