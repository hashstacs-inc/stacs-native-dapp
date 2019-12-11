package io.stacs.nav.drs.service.action.handler;

import io.stacs.nav.drs.service.dao.po.BusinessDefinePO;
import io.stacs.nav.drs.service.enums.ActionExecTypeEnum;

import java.util.List;

import static io.stacs.nav.drs.service.enums.ActionExecTypeEnum.CONTRACT_ADD;

/**
 * @author dekuofa <br>
 * @date 2019-12-11 <br>
 */
public class ContractCreateExecHandler implements ActionExecHandler<BusinessDefinePO> {

    @Override public ActionExecTypeEnum execType() {
        return CONTRACT_ADD;
    }

    @Override public void doHandler(List<BusinessDefinePO> actions) {

    }
}