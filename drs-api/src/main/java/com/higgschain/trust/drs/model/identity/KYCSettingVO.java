package com.higgschain.trust.drs.model.identity;

import com.higgschain.trust.drs.enums.FunctionDefineEnum;
import com.higgschain.trust.drs.model.BaseTxVO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author ganxiang
 * @date 10/23/0023
 */
@Getter @Setter public class KYCSettingVO extends BaseTxVO {

    @NotBlank @Length(max = 40) private String identityAddress;

    @Length(max = 1024) private String KYC;

    /**
     * 1. user(默认) 2. domain 3. node
     */
    private String identityType;

    @Override public String getFunctionName() {
        return FunctionDefineEnum.KYC_SETTING.getFunctionName();
    }
}
