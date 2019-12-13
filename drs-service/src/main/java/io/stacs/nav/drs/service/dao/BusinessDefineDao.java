package io.stacs.nav.drs.service.dao;

import io.stacs.nav.drs.service.BaseDao;
import io.stacs.nav.drs.service.dao.po.BusinessDefinePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * tx request
 *
 * @author liuyu
 */
@Mapper public interface BusinessDefineDao extends BaseDao<BusinessDefinePO> {
    /**
     * query by bd code
     *
     * @param bdCode
     * @return
     */
    BusinessDefinePO queryBDByCode(@Param("bdCode") String bdCode);
}
