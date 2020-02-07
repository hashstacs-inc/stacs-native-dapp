package io.stacs.nav.drs.service.dao;

import io.stacs.nav.drs.service.BaseDao;
import io.stacs.nav.drs.service.dao.po.PolicyPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * tx request
 *
 * @author liuyu
 */
@Mapper public interface PolicyDao extends BaseDao<PolicyPO> {
    /**
     * query all
     *
     * @return
     */
    List<PolicyPO> queryAll();
    /**
     * query by policy id
     * @param policyId
     * @return
     */
    PolicyPO queryByPolicyId(@Param("policyId")String policyId);

    /**
     * batch insert
     * @param list
     * @return
     */
    int batchInsert(List<PolicyPO> list);
}
