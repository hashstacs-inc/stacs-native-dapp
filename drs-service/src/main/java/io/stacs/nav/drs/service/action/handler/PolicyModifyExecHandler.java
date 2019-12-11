package io.stacs.nav.drs.service.action.handler;

import io.stacs.nav.drs.service.dao.po.BusinessDefinePO;
import io.stacs.nav.drs.service.enums.ActionExecTypeEnum;

import java.util.List;

import static io.stacs.nav.drs.service.enums.ActionExecTypeEnum.POLICY_MODIFY;

/**
 * @author dekuofa <br>
 * @date 2019-12-11 <br>
 */
public class PolicyModifyExecHandler implements ActionExecHandler<BusinessDefinePO> {

    @Override public ActionExecTypeEnum execType() {
        return POLICY_MODIFY;
    }

    @Override public void doHandler(List<BusinessDefinePO> actions) {

    }
}
