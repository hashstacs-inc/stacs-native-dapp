package io.stacs.nav.drs.service.dao;

import io.stacs.nav.drs.api.model.ContractVO;
import io.stacs.nav.drs.api.model.query.QueryContractVO;
import io.stacs.nav.drs.service.BaseDao;
import io.stacs.nav.drs.service.dao.po.ContractPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * tx request
 *
 * @author liuyu
 */
@Mapper public interface ContractDao extends BaseDao<ContractPO> {

    int batchInsert(@Param("list") List<ContractPO> contractPOList);

    List<ContractVO> queryByCond(@Param("cond") QueryContractVO cond);

    ContractPO queryByAddress(@Param("address") String address);

}
