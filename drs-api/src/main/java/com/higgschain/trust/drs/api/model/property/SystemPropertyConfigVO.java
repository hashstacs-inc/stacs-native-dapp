package com.higgschain.trust.drs.api.model.property;

import com.higgschain.trust.drs.api.model.BaseTxVO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import static com.higgschain.trust.drs.api.enums.FunctionDefineEnum.SYSTEM_PROPERTY;

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

    @Override public String getFunctionName() {
        return SYSTEM_PROPERTY.getFunctionName();
    }
}
