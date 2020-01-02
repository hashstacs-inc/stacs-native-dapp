package io.stacs.nav.drs.api.model.property;

import io.stacs.nav.drs.api.enums.ApiConstants;
import io.stacs.nav.drs.api.model.BaseTxVO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import static io.stacs.nav.drs.api.enums.ApiConstants.TransactionApiEnum.SYSTEM_PROPERTY;

/**
 * @author ganxiang
 * @date 10/31/0031
 */
@Setter @Getter public class SystemPropertyConfigVO extends BaseTxVO {
    /**
     * property key
     */
    @NotBlank @Length(max = 190) private String key;

    /**
     * property value
     */
    @NotBlank @Length(max = 1024) private String value;
    /**
     * desc for the property
     */
    private String desc;

    @Override public String getMethodSign() {
        return ApiConstants.TransactionApiEnum.SYSTEM_PROPERTY.getFunctionName();
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
                            + key
                            + value
                            + getFunctionName();
    }
}
