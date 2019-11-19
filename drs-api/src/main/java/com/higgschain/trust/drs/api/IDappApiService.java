package com.higgschain.trust.drs.api;

import com.higgschain.trust.drs.exception.DappException;
import com.higgschain.trust.drs.model.BaseTxVO;
import com.higgschain.trust.drs.model.SampleRequest;
import com.higgschain.trust.drs.model.SampleResult;

/**
 * @author suimi
 * @date 2019/10/30
 */
public interface IDappApiService {

    SampleResult service(SampleRequest request);

    /**
     * submit transaction to block chain
     * @param vo
     */
    void submit(BaseTxVO vo) throws DappException;
}
