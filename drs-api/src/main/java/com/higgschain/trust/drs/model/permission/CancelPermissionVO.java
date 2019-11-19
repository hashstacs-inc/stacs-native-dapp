package com.higgschain.trust.drs.model.permission;

import com.higgschain.trust.drs.enums.FunctionDefineEnum;
import com.higgschain.trust.drs.model.BaseTxVO;
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
