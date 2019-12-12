package io.stacs.nav.drs.service.action.handler;

import io.stacs.nav.drs.service.dao.BusinessDefineDao;
import io.stacs.nav.drs.service.dao.po.BusinessDefinePO;
import io.stacs.nav.drs.service.enums.ActionExecTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.stacs.nav.drs.service.enums.ActionExecTypeEnum.BD_ADD;

/**
 * @author dekuofa <br>
 * @date 2019-12-11 <br>
 */
@Service public class BDPublishExecHandler implements ActionExecHandler<BusinessDefinePO> {

    @Autowired private BusinessDefineDao businessDefineDao;

    @Override public ActionExecTypeEnum execType() {
        return BD_ADD;
    }

    @Override public void doHandler(List<BusinessDefinePO> actions) {
        if (actions == null || actions.isEmpty())
            return;
        actions.forEach(businessDefineDao::add);
    }
}
