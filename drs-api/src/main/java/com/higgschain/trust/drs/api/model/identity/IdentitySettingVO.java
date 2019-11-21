package com.higgschain.trust.drs.api.model.identity;

import com.higgschain.trust.drs.api.enums.FunctionDefineEnum;
import com.higgschain.trust.drs.api.model.BaseTxVO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author ganxiang
 * @date 2019/10/16
 */
@Setter @Getter public class IdentitySettingVO extends BaseTxVO {

    @NotBlank private String identityType;

    /**
     * user customer property json
     */
    @Length(max = 1024) private String property;

    /**
     * address of identity
     */
    @NotBlank @Length(max = 40) private String address;

    @Override public String getFunctionName() {
        return FunctionDefineEnum.IDENTITY_SETTING.getFunctionName();
    }
}
