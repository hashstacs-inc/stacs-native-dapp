package io.stacs.nav.drs.api.model.attestation;

import io.stacs.nav.drs.api.enums.ApiConstants;
import io.stacs.nav.drs.api.model.BaseTxVO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author dekuofa <br>
 * @date 2019-11-01 <br>
 */
@Getter @Setter public class SaveAttestationVO extends BaseTxVO {
    /**
     * 版本
     */
    @NotBlank @Length(max = 20) private String attestationVersion;
    /**
     * 存证内容
     */
    @NotBlank @Length(max = 4096) private String attestation;
    /**
     * 备注
     */
    @Length(max = 1024) private String remark;

    @Override public String getMethodSign() {
        return ApiConstants.TransactionApiEnum.SAVE_ATTESTATION.getFunctionName();
    }

    @Override public String getFunctionName() {
        if (StringUtils.isEmpty(super.getFunctionName())) {
            return this.getMethodSign();
        }
        return super.getFunctionName();
    }
    @Override public String getSignValue(){
        return super.getSignValue()
                    + attestation
                    + getFunctionName();
    }
}
