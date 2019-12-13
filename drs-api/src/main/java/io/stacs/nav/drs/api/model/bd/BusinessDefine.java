package io.stacs.nav.drs.api.model.bd;

import io.stacs.nav.drs.api.model.BaseTxVO;
import lombok.Getter;
import lombok.Setter;

import static io.stacs.nav.drs.api.enums.ApiConstants.TransactionApiEnum.BD_PUBLISH;

/**
 * @author dekuofa <br>
 * @date 2019-11-04 <br>
 */
@Getter @Setter public class BusinessDefine extends BaseTxVO {

    private String code;

    private String name;

    private String bdType;

    private String desc;

    private String initPermission;

    private String initPolicy;

    private String functions;

    private String bdVersion;

    @Override public String getFunctionName() {
        return BD_PUBLISH.getFunctionName();
    }

}
