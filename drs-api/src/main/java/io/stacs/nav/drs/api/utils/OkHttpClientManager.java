package io.stacs.nav.drs.api.utils;

import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

/**
 * OkHttpClientManager对OkHttp进行封装
 * ref:https://github.com/hongyangAndroid/okhttputils/blob/5f02ae06c0d19ff7672e0cc422c2abd8e3564f1d/OkHttpClientManager.java
 *
 * @author yangjiyun
 * @create 2017 -06-21 11:58
 */
public class OkHttpClientManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(OkHttpClientManager.class);

    private static OkHttpClientManager mInstance;
    private static OkHttpClientManager mInstanceTimeout;
    private OkHttpClient mOkHttpClient;


    private static final String TAG = "OkHttpClientManager";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public void setCertificates(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

                try {
                    if (certificate != null){
                        certificate.close();
                    }
                } catch (IOException e) {
                }

            }

            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.
                    getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
//            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//            keyManagerFactory.init(keyStore, "keystore_pass".toCharArray());
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
//            mOkHttpClient.setSslSocketFactory(sslContext.getSocketFactory());


        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

    }


    private OkHttpClientManager() {
        mOkHttpClient = new OkHttpClient.Builder().build();

        //cookie enabled
//        mGson = new Gson();
    }

    private OkHttpClientManager(Long timeoutMs) {
        mOkHttpClient = new OkHttpClient.Builder().connectTimeout(timeoutMs, TimeUnit.MILLISECONDS).
                readTimeout(timeoutMs, TimeUnit.MILLISECONDS).
                writeTimeout(timeoutMs, TimeUnit.MILLISECONDS).build();
    }


    public synchronized static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            mInstance = new OkHttpClientManager();
        }
        return mInstance;
    }

    public synchronized static OkHttpClientManager getInstance(Long timeoutMs) {
        if (mInstanceTimeout == null) {
            mInstanceTimeout = new OkHttpClientManager(timeoutMs);
        }
        return mInstanceTimeout;
    }

    /**
     * 同步的Get请求
     *
     * @param url
     * @return Response
     */
    private Response _getAsyn(String url) throws IOException {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        Response execute = call.execute();
        return execute;
    }

    /**
     * 同步的Get请求
     *
     * @param url
     * @return 字符串
     */
    private String _getAsString(String url) throws IOException {
        Response execute = _getAsyn(url);
        return execute.body().string();
    }


    /**
     * 同步的Post请求
     *
     * @param url
     * @param
     * @return
     */
    private Response _post(String url, String json) throws IOException {
        Request request = buildJsonPostRequest(url, json);
        Response response = mOkHttpClient.newCall(request).execute();
        return response;
    }


    /**
     * 同步的Post请求字符串
     *
     * @param url
     * @param
     * @return 字符串
     */
    private String _postAsString(String url, String json) throws IOException {
        Response response = _post(url, json);
        return response.body().string();
    }

    public static Response getAsyn(String url) throws IOException {
        return getInstance()._getAsyn(url);
    }

    public static String getAsString(String url) throws IOException {
        return getInstance()._getAsString(url);
    }

    public static String getAsString(String url, Long timeoutMs) throws IOException {
        return getInstance(timeoutMs)._getAsString(url);
    }

    public static Response post(String url, String json) throws IOException {
        return getInstance()._post(url, json);
    }

    public static String postAsString(String url, String json) throws IOException {
        return getInstance()._postAsString(url, json);
    }

    /**
     * timeout单位是毫秒（ms）
     *
     * @param url
     * @param json
     * @param timeoutMs
     * @return
     * @throws IOException
     */
    public static String postAsString(String url, String json, Long timeoutMs) throws IOException {
        return getInstance(timeoutMs)._postAsString(url, json);
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }


    private Request buildJsonPostRequest(String url, String json) {
        if (StringUtils.isBlank(json)) {
            LOGGER.info(json + " is blank");
            return null;
        }
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return request;
    }


}
