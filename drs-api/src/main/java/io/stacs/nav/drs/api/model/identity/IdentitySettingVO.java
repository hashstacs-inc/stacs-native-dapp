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
 * @date 2019/10/16
 */
@Setter @Getter @ToString(callSuper = true) public class IdentitySettingVO extends BaseTxVO {

    @NotBlank private String identityType;

    /**
     * user customer property json
     */
    @Length(max = 1024) private String property;
    /**
     * 1:hidden,0:show
     */
    private int hidden;
    /**
     * address of identity
     */
    @NotBlank @Length(max = 40) private String address;

    @Override public String getMethodSign() {
        return ApiConstants.TransactionApiEnum.IDENTITY_SETTING.getFunctionName();
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
                    + identityType
                    + property
                    + address
                    + getFunctionName();
    }
}
