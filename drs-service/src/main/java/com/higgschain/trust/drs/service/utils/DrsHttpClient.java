/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.drs.service.utils;

import com.alibaba.fastjson.JSON;
import com.higgschain.trust.drs.service.config.ConfigListener;
import com.higgschain.trust.drs.service.config.DomainConfig;
import com.higgschain.trust.drs.service.utils.LambdaExceptionUtil.BiFunctionWithExceptions;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author suimi
 * @date 2019/1/4
 */

@Component @Slf4j public class DrsHttpClient implements ConfigListener {

    private static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");

    // private static final String TEST_NO_ENCRYPT_MERCHANT = "TEST_NO_ENCRYPT_MERCHANT";

    private static OkHttpClient client = new OkHttpClient.Builder().build();
    private DomainConfig config;

    public static <T> T get(String url, Class<T> clazz) throws IOException {
        String response = get(url);
        return JSON.parseObject(response, clazz);
    }

    public static String get(String url) throws IOException {
        Response response = client.newCall(newGetRequest(url)).execute();
        assert response.body() != null;
        return response.body().string();
    }

    private static Request newPostRequest(String url, @Nonnull String merchantId, @Nonnull Object requestParam) {

        String content = JSON.toJSONString(requestParam);
        log.debug("request content:{}", content);
        return new Request.Builder().url(url).post(RequestBody.create(JSON_TYPE, content))
            .addHeader("merchantId", merchantId).build();
    }

    private static Request newGetRequest(String url) {
        return new Request.Builder().url(url).build();
    }

    @Nonnull public BiFunctionWithExceptions<Object, String, CasDecryptResponse, IOException> post() {
        return (requestParam, url) -> {
            if (requestParam == null) {
                throw new IllegalArgumentException("post request body can't be null");
            }
            final DomainConfig config = this.config; // 先获取&保存当前配置内存地址到栈中，避免并发错误
            String merchantId = config.getMerchantId();
            String aesKey = config.getAesKey();
            String priKey = config.getMerchantPriKey();
            String pubKey = config.getChainPubKey();
            if (merchantId == null) {
                throw new IllegalArgumentException("post request body can't be null");
            }
            // 先使用发起方的私钥签名   再使用AES加密requestParam
            CasEncryptRequest encryptBody = CasCryptoUtil.encrypt(requestParam, priKey, aesKey);
            Request request = newPostRequest(url, merchantId, encryptBody);
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            assert responseBody != null;
            if (!response.isSuccessful()) {
                log.error("request error, response: {}", responseBody.string());
                throw new ResponseStatusException(HttpStatus.valueOf(response.code()));
            }
            String responseStr = responseBody.string();
            log.debug("response body:{}", responseStr);
            return CasCryptoUtil.decrypt(responseStr, pubKey, aesKey);
        };
    }

    @Nonnull public CasDecryptResponse postWithoutCrypto(Object requestParam, String url)
        throws IOException, ResponseStatusException {

        if (requestParam == null) {
            throw new IllegalArgumentException("post request body can't be null");
        }
        final DomainConfig config = this.config; // 先获取&保存当前配置内存地址到栈中，避免并发错误
        String merchantId = config.getMerchantId();
        String aesKey = config.getAesKey();
        String priKey = config.getMerchantPriKey();
        String pubKey = config.getChainPubKey();
        if (merchantId == null) {
            throw new IllegalArgumentException("post request body can't be null");
        }
        // 先使用发起方的私钥签名   再使用AES加密requestParam
        CasEncryptRequest encryptBody = CasCryptoUtil.encrypt(requestParam, priKey, aesKey);
        Request request = newPostRequest(url, merchantId, encryptBody);
        Response response = client.newCall(request).execute();
        ResponseBody responseBody = response.body();
        assert responseBody != null;
        if (!response.isSuccessful()) {
            log.error("request error, response: {}", responseBody.string());
            throw new ResponseStatusException(HttpStatus.valueOf(response.code()));
        }
        String responseStr = responseBody.string();
        log.debug("response body:{}", responseStr);
        return CasCryptoUtil.decrypt(responseStr, pubKey, aesKey);
    }

    public <T> T post(Object requestParam, String url, Function<CasDecryptResponse, T> respConverter)
        throws IOException, ResponseStatusException {
        return respConverter.apply(post().apply(requestParam, url));
    }

    @Override public void updateNotify(Object config) {
        this.config = (DomainConfig)config;
    }

    @Nonnull @Override public Predicate<Object> filter() {
        return config -> config instanceof DomainConfig;
    }
}
