package io.stacs.nav.drs.api.model.permission;

import io.stacs.nav.drs.api.enums.FunctionDefineEnum;
import io.stacs.nav.drs.api.model.BaseTxVO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * auth/cancel identity permissions
 *
 * @author ganxiang
 * @date 2019/10/17
 */
@Getter @Setter public class AuthPermissionVO extends BaseTxVO {

    @NotBlank @Length(max = 40) private String identityAddress;

    @NotEmpty private String[] permissionNames;
    /**
     * 1. user 2. domain 3. node
     */
    @NotEmpty private String identityType;

    @Override public String getFunctionName() {
        return FunctionDefineEnum.AUTHORIZE_PERMISSION.getFunctionName();
    }

    @Override
    public String getSignValue(){
        return super.getSignValue()
                        + identityAddress
                        + identityType
                        + String.join(",",permissionNames)
                        + getFunctionName();
    }
}
