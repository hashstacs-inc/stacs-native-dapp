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
@Getter @Setter public class CancelPermissionVO extends BaseTxVO {
    @NotBlank @Length(max = 40) private String identityAddress;

    @NotEmpty private String[] permissionNames;

    @Override public String getFunctionName() {
        return FunctionDefineEnum.CANCEL_PERMISSION.getFunctionName();
    }
}
