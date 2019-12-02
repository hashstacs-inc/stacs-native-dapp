package io.stacs.nav.drs.service.network;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import io.stacs.nav.drs.api.enums.ApiConstants.QueryApiEnum;
import io.stacs.nav.drs.api.model.Policy;
import io.stacs.nav.drs.api.model.RespData;
import io.stacs.nav.drs.api.model.RsDomain;
import io.stacs.nav.drs.api.model.bd.BusinessDefine;
import io.stacs.nav.drs.service.config.ConfigListener;
import io.stacs.nav.drs.service.config.DomainConfig;
import io.stacs.nav.drs.service.utils.CasDecryptResponse;
import io.stacs.nav.drs.service.utils.DrsHttpClient;
import io.stacs.nav.drs.service.utils.LambdaExceptionUtil;
import io.stacs.nav.drs.service.utils.Pair;
import io.stacs.nav.drs.service.vo.PermissionCheckVO;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static io.stacs.nav.drs.api.enums.ApiConstants.ENCRYPT_WHITE_LIST;
import static io.stacs.nav.drs.api.enums.ApiConstants.QueryApiEnum.*;
import static io.stacs.nav.drs.service.utils.HttpHelper.buildGetRequestParam;

/**
 * @author liuyu
 * @date 2019-11-07
 */
@Component @Slf4j public class BlockChainFacade implements ConfigListener {

    @Autowired private DrsHttpClient client;
    private static Predicate<String> encryptSkipFilter = Pattern.compile("[Qq]uery").asPredicate();

    private String baseUrl;

    private static Function<CasDecryptResponse, RespData<?>> defaultPostConverter() {
        return decryptResp -> JSONObject.parseObject(decryptResp.getData().toString(), RespData.class);
    }

    @SuppressWarnings("unchecked") private static <T> Function<ResponseBody, RespData<T>> defaultGetConverter()
        throws IOException {
        return LambdaExceptionUtil.rethrowFunction(
            body -> (RespData<T>)JSONObject.parseObject(body.string(), RespData.class));
    }

    @SuppressWarnings("unchecked") private static <T> Function<RespData<?>, T> getRealData() {
        return respData -> (T)respData.getData();
    }

    @SuppressWarnings("unchecked") private static <T> Function<RespData<?>, Optional<T>> checkSuccessAndGetRealData() {
        return resp -> resp.isSuccessful() ? Optional.of((T)resp.getData()) : Optional.empty();
    }

    public <T> Optional<T> commonQueryApi(QueryApiEnum query, @Nullable List<Pair<String, String>> params) {
        try {
            return sendGet(query.getApi(), params, checkSuccessAndGetRealData());
        } catch (IOException e) {
            log.error("[{}]has unknown error", query.getName(), e);
            return Optional.empty();
        }
    }

    public Optional<BusinessDefine> queryBDInfoByCode(String bdCode) {
        try {
            Optional<List<BusinessDefine>> result = queryBDInfo(bdCode);
            return result.map(list -> list.get(0));
        } catch (Exception e) {
            log.error("[queryBDInfoByCode]has unknown error", e);
            return Optional.empty();
        }
    }

    public Optional<List<BusinessDefine>> queryBDInfo(String bdCode) {
        List<Pair<String, String>> params = null;
        if (StringUtils.isNotEmpty(bdCode)) {
            params = Lists.newArrayList(Pair.newPair("bdCode", bdCode));
        }
        return commonQueryApi(BD_QUERY, params);
    }

    public Optional<Long> queryCurrentHeight() {
        return commonQueryApi(QUERY_MAX_BLOCK_HEIGHT, null);
    }

    public Optional<Long> queryBlocks() {
        return commonQueryApi(QUERY_MAX_BLOCK_HEIGHT, null);
    }

    /**
     * query domains of block chain
     */
    public Optional<List<RsDomain>> queryAllDomains() {
        return commonQueryApi(QUERY_ALL_DOMAIN, null);
    }

    /**
     * query domains of block chain
     */
    public Optional<List<Policy>> queryAllPolicyList() {
        return commonQueryApi(QUERY_POLICY_LIST, null);
    }

    /**
     * query all permission of block chain
     */
    public Optional<List<Policy>> queryPermissionList() {
        return commonQueryApi(QUERY_PERMISSION_LIST, null);
    }

    public Optional<Boolean> checkPermission(PermissionCheckVO vo) {
        try {
            return Optional.of(
                (Boolean)send(CHECK_PERMISSION.getApi(), vo, getRealData().compose(defaultPostConverter())));
        } catch (IOException e) {
            log.error("[checkPermission]has unknown error", e);
            return Optional.empty();
        }
    }

    @Nonnull
    public <T> T sendGet(String api, @Nullable List<Pair<String, String>> params, Function<RespData<?>, T> converter)
        throws IOException {
        String url = baseUrl + api;
        String requestParam = buildGetRequestParam(params);
        if (StringUtils.isNotEmpty(requestParam)) {
            if (url.contains("?")) {
                url = url + "&" + requestParam;
            } else {
                url = url + "?" + requestParam;
            }
        }
        return sendGet(url, converter);
    }

    private <T> T sendGet(String url, Function<RespData<?>, T> converter) throws IOException {
        return DrsHttpClient.get(url, defaultGetConverter().andThen(converter));
    }

    public RespData<?> send(String api, Object txData) throws IOException {
        return send(api, txData, defaultPostConverter());
    }

    public <T> T send(String api, Object txData, Function<CasDecryptResponse, T> converter) throws IOException {
        if (encryptSkipFilter.test(api) || ENCRYPT_WHITE_LIST.stream().anyMatch(url -> url.equals(api))) {
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
