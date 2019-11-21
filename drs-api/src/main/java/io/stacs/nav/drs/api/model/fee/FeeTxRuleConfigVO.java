package io.stacs.nav.drs.api.model.fee;

import io.stacs.nav.drs.api.model.BaseTxVO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.util.List;

import static io.stacs.nav.drs.api.enums.FunctionDefineEnum.SET_FEE_RULE;

/**
 * @author liuyu
 * @date 2019-09-05
 */
@Getter @Setter public class FeeTxRuleConfigVO extends BaseTxVO {
    /**
     * fee tx rules
     */
    @NotEmpty @Valid private List<FeeTxRuleVO> rules;

    @Override public String getFunctionName() {
        return SET_FEE_RULE.getFunctionName();
    }
}
