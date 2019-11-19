package com.higgschain.trust.drs.model.permission;

import com.higgschain.trust.drs.enums.FunctionDefineEnum;
import com.higgschain.trust.drs.model.BaseTxVO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author ganxiang
 * @date 2019/10/16
 */
@Setter @Getter public class RegisterPermissionVO extends BaseTxVO {

    @NotBlank @Length(max = 64) private String permissionName;

    @Override public String getFunctionName() {
        return FunctionDefineEnum.PERMISSION_REGISTER.getFunctionName();
    }
}
