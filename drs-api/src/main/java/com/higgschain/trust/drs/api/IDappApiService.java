package com.higgschain.trust.drs.api;

import com.higgschain.trust.drs.exception.DappException;
import com.higgschain.trust.drs.model.SampleRequest;
import com.higgschain.trust.drs.model.SampleResult;
import com.higgschain.trust.drs.model.bd.BusinessDefine;

/**
 * @author suimi
 * @date 2019/10/30
 */
public interface IDappApiService {

    SampleResult service(SampleRequest request);

    /**
     * publish business define
     *
     * @param bd
     * @throws DappException
     */
    void publishBD(BusinessDefine bd) throws DappException;
}
