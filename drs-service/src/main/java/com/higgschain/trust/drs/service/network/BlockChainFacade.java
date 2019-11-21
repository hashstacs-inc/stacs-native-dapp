package com.higgschain.trust.drs.service.network;

import com.alibaba.fastjson.JSONObject;
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
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.higgschain.trust.drs.service.utils.HttpHelper.buildGetRequestParam;
import static com.higgschain.trust.drs.service.utils.LambdaExceptionUtil.rethrowFunction;

/**
 * @author liuyu
 * @date 2019-11-07
 */
@Component @Slf4j public class BlockChainFacade implements ConfigListener {
    private final static String API_BD_QUERY = "/bd/query";
    private final static String API_CHECK_PERMISSION = "/identity/open/checkPermission";
    private final static Stream<String> ENCRYPT_WHITE_LIST = Stream.of("callback/register");

    @Autowired private DrsHttpClient client;
    private static Predicate<String> encryptSkipFilter = Pattern.compile("[Qq]uery").asPredicate();

    private String baseUrl;

    private static Function<CasDecryptResponse, RespData<?>> defaultPostConverter() {
        return decryptResp -> JSONObject.parseObject(decryptResp.getData().toString(), RespData.class);
    }

    @SuppressWarnings("unchecked") private static <T> Function<ResponseBody, RespData<T>> defaultGetConverter()
        throws IOException {
        return rethrowFunction(body -> (RespData<T>)JSONObject.parseObject(body.string(), RespData.class));
    }

    @SuppressWarnings("unchecked") private static <T> Function<RespData, T> getRealData() {
        return respData -> (T)respData.getData();
    }

    @SuppressWarnings("unchecked") private static <T> Function<RespData<?>, Optional<T>> checkSuccessAndGetRealData() {
        return resp -> resp.isSuccessful() ? Optional.of((T)resp.getData()) : Optional.empty();
    }

    public Optional<BusinessDefine> queryBDInfoByCode(String bdCode) {
        try {
            Optional<List<BusinessDefine>> result =
                sendGet(API_BD_QUERY, Lists.newArrayList(Pair.newPair("bdCode", bdCode)), checkSuccessAndGetRealData());
            return result.map(list -> list.get(0));
        } catch (IOException e) {
            log.error("[queryBDInfoByCode]has unknown error",e);
            return Optional.empty();
        }
    }

    public Optional<Boolean> checkPermission(PermissionCheckVO vo) {
        try {
            return Optional.of((Boolean)send(API_CHECK_PERMISSION, vo, getRealData().compose(defaultPostConverter())));
        } catch (IOException e) {
            log.error("[checkPermission]has unknown error",e);
            return Optional.empty();
        }
    }

    @Nonnull public <T> T sendGet(String api, List<Pair<String, String>> params, Function<RespData<?>, T> converter)
        throws IOException {
        String url = baseUrl + api;
        String requestParam = buildGetRequestParam(params);
        if (url.endsWith("?")) {
            url = url + requestParam;
        } else {
            url = url + "?" + requestParam;
        }
        return sendGet(url, converter);
    }

    private <T> T sendGet(String url, Function<RespData<?>, T> converter) throws IOException {
        return DrsHttpClient.get(url, defaultGetConverter().andThen(converter));
    }


    public RespData<?> send(String api, Object txData) throws IOException {
        return send(api, txData, defaultPostConverter());
    }

    public <T> T send(String api, Object txData, Function<CasDecryptResponse, T> converter)
        throws IOException {
        if (encryptSkipFilter.test(api) || ENCRYPT_WHITE_LIST.anyMatch(url -> url.equals(api))) {
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
