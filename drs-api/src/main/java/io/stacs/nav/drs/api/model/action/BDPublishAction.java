package io.stacs.nav.drs.api.model.action;

import io.stacs.nav.drs.api.enums.ActionTypeEnum;
import io.stacs.nav.drs.api.model.Action;
import io.stacs.nav.drs.api.model.bd.FunctionDefine;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author suimi
 * @date 2019/10/23
 */
@Getter @Setter public class BDPublishAction extends Action {

    private String code;
    private String name;
    private String bdType;
    private String desc;
    private String initPermission;
    private String initPolicy;
    private List<FunctionDefine> functions;
    private String bdVersion;
    private Map<String, FunctionDefine> functionsMap;

    public BDPublishAction() {
        this.setType(ActionTypeEnum.BD_PUBLISH);
        this.setIndex(0);
    }

}
