package io.stacs.nav.drs.api.model.identity;


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
 * @date 10/23/0023
 */
@Getter @Setter @ToString(callSuper = true) public class KYCSettingVO extends BaseTxVO {

    @NotBlank @Length(max = 40) private String identityAddress;

    @Length(max = 1024) private String KYC;

    /**
     * 1. user(默认) 2. domain 3. node
     */
    private String identityType;

    @Override public String getMethodSign() {
        return ApiConstants.TransactionApiEnum.KYC_SETTING.getFunctionName();
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
                    + identityAddress
                    + KYC
                    + identityType
                    + getFunctionName();
    }
}
