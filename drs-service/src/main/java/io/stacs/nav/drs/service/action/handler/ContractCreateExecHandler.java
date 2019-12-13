package io.stacs.nav.drs.service.action.handler;

import io.stacs.nav.drs.service.dao.ContractDao;
import io.stacs.nav.drs.service.dao.po.ContractPO;
import io.stacs.nav.drs.service.enums.ActionExecTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.stacs.nav.drs.service.enums.ActionExecTypeEnum.CONTRACT_ADD;

/**
 * @author dekuofa <br>
 * @date 2019-12-11 <br>
 */
@Service public class ContractCreateExecHandler implements ActionExecHandler<ContractPO> {
    @Autowired private ContractDao contractDao;

    @Override public ActionExecTypeEnum execType() {
        return CONTRACT_ADD;
    }

    @Override public void doHandler(List<ContractPO> actions) {
        if (actions == null || actions.isEmpty())
            return;
        contractDao.batchInsert(actions);
    }
}
