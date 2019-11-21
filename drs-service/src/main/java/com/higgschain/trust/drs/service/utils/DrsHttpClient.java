/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.drs.service.utils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.higgschain.trust.drs.service.config.ConfigListener;
import com.higgschain.trust.drs.service.config.DomainConfig;
import com.higgschain.trust.drs.service.utils.LambdaExceptionUtil.BiFunctionWithExceptions;
import com.higgschain.trust.drs.service.utils.LambdaExceptionUtil.FunctionWithExceptions;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.higgschain.trust.drs.service.utils.LambdaExceptionUtil.rethrowFunction;
import static com.higgschain.trust.drs.service.utils.Pair.newPair;

/**
 * @author suimi
 * @date 2019/1/4
 */

@Component @Slf4j public class DrsHttpClient implements ConfigListener {

    private static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");

    private static Function<String, Pair<String, String>> HEADER_MERCHANT_ID = header -> newPair("merchantId", header);

    // private static final String TEST_NO_ENCRYPT_MERCHANT = "TEST_NO_ENCRYPT_MERCHANT";

    private static OkHttpClient client = new OkHttpClient.Builder().build();
    private DomainConfig config;

    public static <T> T get(String url, Function<ResponseBody, T> converter) throws IOException {
        ResponseBody responseBody = sendGet(url);
        return converter.apply(responseBody);
    }

    @Nonnull public static ResponseBody sendGet(String url) throws IOException {
        Response response = client.newCall(newGetRequest(url)).execute();
        assert response.body() != null;
        return response.body();
    }

    private static Request newPostRequest(String url, @Nonnull Object requestParam,
        List<Pair<String, String>> headers) {

        String content = JSON.toJSONString(requestParam);
        log.debug("request content:{}", content);
        Request.Builder request = new Request.Builder().url(url).post(RequestBody.create(JSON_TYPE, content));
        if (CollectionUtils.isNotEmpty(headers)) {
            for (Pair<String, String> pair : headers) {
                request.addHeader(pair.getLeft(), pair.getRight());
            }
        }
        return request.build();
    }

    private static Request newGetRequest(String url) {
        return new Request.Builder().url(url).build();
    }

    @Nonnull public BiFunctionWithExceptions<Object, String, CasDecryptResponse, IOException> postWithEncrypt() {
        return (param, url) -> {
            Objects.requireNonNull(param, "post request body can't be null");
            Pair<CasEncryptRequest, Optional<DomainConfig>> pair = encrypt().apply(param);
            Response resp = buildRequest().andThen(rethrowFunction(execute())).apply(url, pair);
            return parseResponse().apply(resp, pair.getRight());
        };
    }

    @Nonnull public BiFunctionWithExceptions<Object, String, CasDecryptResponse, IOException> post() {
        return (param, url) -> {
            Objects.requireNonNull(param, "post request body can't be null");
            Response resp =
                buildRequest().andThen(rethrowFunction(execute())).apply(url, newPair(param, Optional.empty()));
            return parseResponse().apply(resp, Optional.empty());
        };
    }

    public Function<Object, Pair<CasEncryptRequest, Optional<DomainConfig>>> encrypt() {
        return param -> {
            // load config
            final DomainConfig config = this.config; // 先获取&保存当前配置内存地址到栈中，避免并发错误
            Objects.requireNonNull(config.getMerchantId(), "merchantId can't be null");
            String aesKey = config.getAesKey();
            String priKey = config.getMerchantPriKey();
            return newPair(CasCryptoUtil.encrypt(param, priKey, aesKey), Optional.of(config));
        };
    }

    public BiFunction<String, Pair<?, Optional<DomainConfig>>, Request> buildRequest() {
        return (url, pair) -> {
            Object param = pair.getLeft();
            List<Pair<String, String>> header = Lists.newArrayList();
            pair.getRight().ifPresent(conf -> header.add(HEADER_MERCHANT_ID.apply(conf.getMerchantId())));
            return newPostRequest(url, param, header);
        };
    }

    public FunctionWithExceptions<Request, Response, IOException> execute() {
        return request -> client.newCall(request).execute();
    }

    public BiFunctionWithExceptions<Response, Optional<DomainConfig>, CasDecryptResponse, IOException> parseResponse() {
        return (response, optional) -> {
            ResponseBody responseBody = response.body();
            assert responseBody != null;
            if (!response.isSuccessful()) {
                log.error("request error, response: {}", responseBody.string());
                throw new ResponseStatusException(HttpStatus.valueOf(response.code()));
            }
            String responseStr = responseBody.string();
            log.debug("response body:{}", responseStr);
            if (optional.isPresent()) {
                DomainConfig config = optional.get();
                return CasCryptoUtil.decrypt(responseStr, config.getChainPubKey(), config.getAesKey());
            }
            return JSON.parseObject(responseStr, CasDecryptResponse.class);
        };
    }

    public <T> T postWithEncrypt(Object requestParam, String url, Function<CasDecryptResponse, T> respConverter)
        throws IOException, ResponseStatusException {
        return postWithEncrypt().andThen(respConverter).apply(requestParam, url);
    }

    public <T> T post(Object requestParam, String url, Function<CasDecryptResponse, T> respConverter)
        throws IOException, ResponseStatusException {
        return post().andThen(respConverter).apply(requestParam, url);
    }

    @Override public void updateNotify(Object config) {
        this.config = (DomainConfig)config;
    }

    @Nonnull @Override public Predicate<Object> filter() {
        return config -> config instanceof DomainConfig;
    }
}
