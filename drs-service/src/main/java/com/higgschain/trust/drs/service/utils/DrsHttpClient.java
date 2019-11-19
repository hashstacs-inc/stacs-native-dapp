/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.drs.service.utils;

import com.alibaba.fastjson.JSON;
import com.higgschain.trust.drs.service.config.ConfigListener;
import com.higgschain.trust.drs.service.config.DomainConfig;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.io.IOException;
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

    /**
     * @param requestParam 请求数据 - 如果请求没有参数，new Object();如果是Json数据，输入应该时JSONObject,不可以为Json字符串
     * @param url          请求地址
     */
    public CasDecryptResponse post(Object requestParam, String url) throws IOException, RuntimeException {

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
            throw new RuntimeException("response error: " + response.code() + "-" + response.message());
        }
        String responseStr = responseBody.string();
        log.debug("response body:{}", responseStr);
        return CasCryptoUtil.decrypt(responseStr, pubKey, aesKey);
    }

    @Override public void updateNotify(Object config) {
        this.config = (DomainConfig)config;
    }

    @Nonnull @Override public Predicate<Object> filter() {
        return config -> config instanceof DomainConfig;
    }
}
