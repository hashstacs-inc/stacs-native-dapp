package io.stacs.nav.drs.service.action.handler;

import io.stacs.nav.drs.service.dao.ActionPO;
import io.stacs.nav.drs.service.enums.ActionExecTypeEnum;

import java.util.List;

/**
 * @author dekuofa <br>
 * @date 2019-12-11 <br>
 */
public interface ActionExecHandler<T extends ActionPO> {

    ActionExecTypeEnum execType();

    void doHandler(List<T> actions);

}
