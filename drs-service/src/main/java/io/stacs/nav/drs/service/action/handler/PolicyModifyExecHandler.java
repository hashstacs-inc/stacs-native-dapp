package io.stacs.nav.drs.service.action.handler;

import com.alibaba.fastjson.JSON;
import io.stacs.nav.drs.service.dao.PolicyDao;
import io.stacs.nav.drs.service.dao.po.BusinessDefinePO;
import io.stacs.nav.drs.service.dao.po.PolicyPO;
import io.stacs.nav.drs.service.enums.ActionExecTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.stacs.nav.drs.service.enums.ActionExecTypeEnum.POLICY_MODIFY;

/**
 * @author dekuofa <br>
 * @date 2019-12-11 <br>
 */
@Service @Slf4j public class PolicyModifyExecHandler implements ActionExecHandler<PolicyPO> {
    @Autowired private PolicyDao policyDao;

    @Override public ActionExecTypeEnum execType() {
        return POLICY_MODIFY;
    }

    @Override public void doHandler(List<PolicyPO> actions) {
        if (CollectionUtils.isEmpty(actions)) {
            return;
        }
        log.info("PolicyModify actions:{}", JSON.toJSONString(actions));
        actions.forEach(v -> {
            try {
                policyDao.updateByPolicyId(v);
            } catch (Throwable e) {
                log.error("register policy has error", e);
            }
        });
    }
}
