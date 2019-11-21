package io.stacs.nav.drs.api.model.permission;

import io.stacs.nav.drs.api.enums.FunctionDefineEnum;
import io.stacs.nav.drs.api.model.BaseTxVO;
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
