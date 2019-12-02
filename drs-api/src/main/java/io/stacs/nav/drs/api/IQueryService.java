package io.stacs.nav.drs.api;

import io.stacs.nav.drs.api.exception.DappException;
import io.stacs.nav.drs.api.model.BaseTxVO;

/**
 * @author dekuofa1995
 * @date 2019/12/2
 */
public interface IQueryService {

    /**
     * generate sign value
     */
    String generateSignature(BaseTxVO vo) throws DappException;

}
