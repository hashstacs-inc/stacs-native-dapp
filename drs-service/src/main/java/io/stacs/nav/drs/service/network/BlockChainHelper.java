package io.stacs.nav.drs.service.network;

import com.alibaba.fastjson.JSON;
import io.stacs.nav.drs.api.exception.DappError;
import io.stacs.nav.drs.api.model.RespData;
import io.stacs.nav.drs.api.utils.OkHttpClientManager;
import io.stacs.nav.drs.service.config.DomainConfig;
import io.stacs.nav.drs.service.config.DrsConfig;
import io.stacs.nav.drs.service.utils.CasCryptoUtil;
import io.stacs.nav.drs.service.utils.CasDecryptResponse;
import io.stacs.nav.drs.service.utils.CasEncryptRequest;
import io.stacs.nav.drs.service.utils.Pair;
import io.stacs.nav.drs.service.utils.config.ConfigListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static io.stacs.nav.drs.service.utils.HttpHelper.buildGetRequestParam;

/**
 * @author liuyu
 * @description
 * @date 2019-12-19
 */
@Component @Slf4j public class BlockChainHelper implements ConfigListener {
    private final static long TIME_OUT = 60 * 1000;
    @Autowired DrsConfig drsConfig;
    @Autowired DomainConfig domainConfig;

    /**
     * post request
     *
     * @param api
     * @param param
     * @return
     */
    public <T> RespData<T> post(String api, Object param, Class<T> clazz) {
        try {
            String url = domainConfig.getBaseUrl() + api;
            log.info("[post]url:{}", url);
            log.info("[post]requestJSON:{}", JSON.toJSONString(param));
            CasEncryptRequest request =
                CasCryptoUtil.encrypt(param, domainConfig.getMerchantPriKey(), domainConfig.getAesKey());
            String requestJSON = JSON.toJSONString(request);
            log.info("[post]requestJSONEncrypt:{}", requestJSON);
            String merchantId = domainConfig.getMerchantId();
            log.info("[post]merchantId:{}", merchantId);
            String res = OkHttpClientManager.postAsString(url, requestJSON,merchantId , TIME_OUT);
            if (StringUtils.isEmpty(res)) {
                log.error("[post]response is null");
                return RespData.fail(DappError.DAPP_COMMON_ERROR);
            }
            log.info("[post]responseJSON:{}", res);
            CasDecryptResponse response =
                CasCryptoUtil.decrypt(res, domainConfig.getChainPubKey(), domainConfig.getAesKey());
            RespData respData = new RespData();
            respData.setCode(response.getRespCode());
            respData.setMsg(response.getMsg());
            String dataJSON = JSON.toJSONString(response.getData());
            log.info("[post]dataJSON:{}", dataJSON);
            respData.setData(JSON.parseObject(dataJSON, clazz));
        } catch (Exception e) {
            log.error("post has error", e);
        }
        return RespData.fail(DappError.DAPP_COMMON_ERROR);
    }

    /**
     * get request
     *
     * @param api
     * @param params
     * @return
     */
    public RespData<?> get(String api, List<Pair<String, String>> params) {
        try {
            String url = domainConfig.getBaseUrl() + api;
            String requestParam = buildGetRequestParam(params);
            if (StringUtils.isNotEmpty(requestParam)) {
                if (url.contains("?")) {
                    url = url + "&" + requestParam;
                } else {
                    url = url + "?" + requestParam;
                }
            }
            log.info("[get]url:{}", url);
            String merchantId = domainConfig.getMerchantId();
            log.info("[post]merchantId:{}", merchantId);
            String res = OkHttpClientManager.getAsString(url, merchantId, TIME_OUT);
            if (StringUtils.isEmpty(res)) {
                log.error("[get]response is null");
                return RespData.fail(DappError.DAPP_COMMON_ERROR);
            }
            return JSON.parseObject(res, RespData.class);
        } catch (Exception e) {
            log.error("get has error", e);
        }
        return RespData.fail(DappError.DAPP_COMMON_ERROR);
    }

    @Override public <T> void updateNotify(T config) {
        if (config instanceof DrsConfig) {
            this.drsConfig = (DrsConfig)config;
        }
        if (config instanceof DomainConfig) {
            this.domainConfig = (DomainConfig)config;
        }
    }
}
