package com.higgschain.trust.drs.service.network;

import com.hashstacs.sdk.http.HttpFacade;
import com.hashstacs.sdk.wallet.dock.bo.CasDecryptReponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author liuyu
 * @description
 * @date 2019-11-07
 */
@Component @Slf4j public class BlockChainFacade {
    @Value("${drs.domain.baseUrl}") private String baseUrl = null;
    /**
     * the public key of stacs chain
     */
    @Value("${drs.domain.chainPubKey}") protected String chainPubKey;

    /**
     * the private key of access party
     */
    @Value("${drs.domain.merchantPriKey}") protected String merchantPriKey;

    /**
     * aes key, used for communication data encryption, assigned by stacs chain
     */
    @Value("${drs.domain.aesKey}") protected String aesKey;

    /**
     * the merchatId of access party, assigned by stacs chain
     */
    @Value("${drs.domain.merchantId:default}") protected String merchantId;

    /**
     * send request to chain
     *
     * @param api
     * @param txData
     */
    public CasDecryptReponse send(String api, Object txData) throws IOException {
        return HttpFacade.post(merchantId, txData, merchantPriKey, chainPubKey, aesKey, baseUrl + api);
    }
}
