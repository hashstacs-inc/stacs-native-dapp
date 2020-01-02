package io.stacs.nav.drs.api.model.fee;

import io.stacs.nav.drs.api.enums.ApiConstants;
import io.stacs.nav.drs.api.model.BaseTxVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liuyu
 * @date 2019-09-05
 */
@Getter @Setter @ToString(callSuper = true) public class FeeTxRuleConfigVO extends BaseTxVO {
    /**
     * fee tx rules
     */
    @NotEmpty @Valid private List<FeeTxRuleVO> rules;

    @Override public String getMethodSign() {
        return ApiConstants.TransactionApiEnum.SET_FEE_RULE.getFunctionName();
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
                    + String.join(",",rules.stream().map(FeeTxRuleVO::getSignValue).collect(Collectors.toList()))
                    + getFunctionName();
    }
}
