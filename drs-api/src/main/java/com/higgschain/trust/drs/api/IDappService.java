package com.higgschain.trust.drs.api;

import com.higgschain.trust.drs.model.SampleRequest;
import com.higgschain.trust.drs.model.SampleResult;

/**
 * @author suimi
 * @date 2019/10/30
 */
public interface IDappService {

    SampleResult service(SampleRequest request);
}
