package io.stacs.nav.drs.service.service;

import io.stacs.nav.drs.api.model.bd.BusinessDefine;
import io.stacs.nav.drs.service.network.BlockChainFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static io.stacs.nav.drs.api.exception.DappError.BD_NOT_FIND_ERROR;
import static io.stacs.nav.drs.api.exception.DappException.newError;

/**
 * @author liuyu
 * @description
 * @date 2019-11-25
 */
@Service
public class BDService {
    @Autowired BlockChainFacade blockChainFacade;
    //TODO:use cache
    /**
     * query bd info by code
     *
     * @param bdCode
     * @return
     */
    public BusinessDefine queryBDByCode(String bdCode){
        return blockChainFacade.queryBDInfoByCode(bdCode).orElseThrow(newError(BD_NOT_FIND_ERROR));
    }
}
