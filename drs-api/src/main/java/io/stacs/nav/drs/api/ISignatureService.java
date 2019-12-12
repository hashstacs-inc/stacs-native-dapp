package io.stacs.nav.drs.api;

import io.stacs.nav.drs.api.exception.DappException;
import io.stacs.nav.drs.api.model.BaseTxVO;

/**
 * @author suimi
 * @date 2019/12/12
 */
public interface ISignatureService {

    /**
     * generate sign value
     */
    String generateSignature(BaseTxVO vo) throws DappException;


}
