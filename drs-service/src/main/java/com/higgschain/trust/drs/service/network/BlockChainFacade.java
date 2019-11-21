package com.higgschain.trust.drs.service.network;

import com.alibaba.fastjson.JSONObject;
import com.higgschain.trust.drs.api.model.RespData;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.higgschain.trust.drs.api.model.RespData;
import com.higgschain.trust.drs.api.model.bd.BusinessDefine;
import com.higgschain.trust.drs.service.config.ConfigListener;
import com.higgschain.trust.drs.service.config.DomainConfig;
import com.higgschain.trust.drs.service.utils.CasDecryptResponse;
import com.higgschain.trust.drs.service.utils.DrsHttpClient;
import com.higgschain.trust.drs.service.utils.Pair;
import com.higgschain.trust.drs.service.vo.PermissionCheckVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * @author liuyu
 * @date 2019-11-07
 */
@Component @Slf4j public class BlockChainFacade implements ConfigListener {
    private final static String API_BD_QUERY = "/bd/query";
    private final static String API_CHECK_PERMISSION = "/identity/open/checkPermission";

    @Autowired private DrsHttpClient client;
    private static Predicate<String> encryptFilter = Pattern.compile("[Qq]uery").asPredicate();

    private String baseUrl;

    /**
     * query BD info from block chain
     *
     * @param bdCode
     * @return
     */
    public BusinessDefine queryBDInfoByCode(String bdCode) {
        try {
            RespData res = get(API_BD_QUERY, Lists.newArrayList(Pair.newPair("bdCode", bdCode)));
            if (res != null && res.isSuccessful()) {
                List<BusinessDefine> list = (List<BusinessDefine>)res.getData();
                return list.get(0);
            }
        } catch (Throwable e) {
            log.error("[queryBDInfoByCode]has unknown error",e);
            return null;
        }
        return null;
    }

    /**
     * check user permission
     *
     * @param vo
     * @return
     */
    public boolean checkPermission(PermissionCheckVO vo) {
        try {
            RespData res = send(API_CHECK_PERMISSION,vo);
            return (Boolean)res.getData();
        } catch (Throwable e) {
            log.error("[checkPermission]has unknown error",e);
            return false;
        }
    }

    private static Function<CasDecryptResponse, RespData<?>> converter() {
        return decryptResp -> JSONObject.parseObject(decryptResp.getData().toString(), RespData.class);
    }

    public RespData<?> get(String api,List<Pair<String,String>> params) throws IOException {
        String url = baseUrl + api;
        if(CollectionUtils.isNotEmpty(params)){
            Joiner joiner = Joiner.on('&');
            Joiner joiner2 = Joiner.on('=');
            Iterator iterator = params.stream().map(pair -> joiner2.join(pair.getLeft(), pair.getRight())).iterator();
            if(!url.contains("?")){
                url += "?";
            }
            url += joiner.join(iterator);
        }
        return DrsHttpClient.get(url,RespData.class);
    }

    public RespData<?> send(String api, Object txData) throws IOException {
        return send(api, txData, converter());
    }

    public <T> T send(String api, Object txData, Function<CasDecryptResponse, T> converter)
        throws IOException {
        if (encryptFilter.test(api)) {
            return client.post(txData, baseUrl + api, converter);
        }
        return client.postWithEncrypt(txData, baseUrl + api, converter);
    }

    @Override public <T> void updateNotify(T config) {
        this.baseUrl = ((DomainConfig)config).getBaseUrl();
    }

    @Nonnull @Override public Predicate<Object> filter() {
        return obj -> obj instanceof DomainConfig;
    }
}
