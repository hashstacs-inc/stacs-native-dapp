package io.stacs.nav.drs.service.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.io.BaseEncoding;
import io.stacs.nav.crypto.StacsECKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

/**
 * @author WangQuanzhou
 * @date 2018/2/14 14:52
 */
@Slf4j public class CasCryptoUtil {

    public final static int JSON_GENERATE_FEATURES;
    private static final BaseEncoding HEX = BaseEncoding.base16().lowerCase();

    static {
        int features = 0;
        features |= SerializerFeature.DisableCircularReferenceDetect.getMask();
        features |= SerializerFeature.WriteMapNullValue.getMask();
        features |= SerializerFeature.SortField.getMask();
        features |= SerializerFeature.MapSortField.getMask();
        JSON_GENERATE_FEATURES = features;
    }

    /**
     * @param message
     * @param priKey
     * @param aesKey
     * @return CasEncryptRequest
     * @desc
     */
    public static CasEncryptRequest encrypt(Object message, String priKey, String aesKey) {
        if (null == message) {
            throw new IllegalArgumentException("obj can not be null !");
        }
        if (Strings.isBlank(priKey)) {
            throw new IllegalArgumentException("priKey can not be null !");
        }
        if (Strings.isBlank(aesKey)) {
            throw new IllegalArgumentException("aesKey can not be null !");
        }

        CasEncryptRequest casEncryptRequest = new CasEncryptRequest();
        StacsECKey priEcKey = StacsECKey.fromPrivate(HEX.decode(priKey));

        String originMsg =
            message instanceof String ? (String)message : JSON.toJSONString(message, JSON_GENERATE_FEATURES);

        if (log.isDebugEnabled()) {
            log.debug("request origin message:{}", originMsg);
        }
        casEncryptRequest.setSignature(priEcKey.signMessage(originMsg));
        String encryptString = AesUtil.encryptToString(originMsg, aesKey);
        casEncryptRequest.setRequestParam(encryptString);

        return casEncryptRequest;
    }

    /**
     * @param resp
     * @param pubKey
     * @param aesKey
     * @return CasDecryptReponse
     * @desc
     */
    public static CasDecryptResponse decrypt(String resp, String pubKey, String aesKey) {
        CasDecryptResponse response = new CasDecryptResponse();

        try {
            JSONObject jsonObject = JSON.parseObject(resp);
            if (!jsonObject.containsKey("data") || !jsonObject.containsKey("signature") || !jsonObject
                .containsKey("respCode") || !jsonObject.containsKey("msg")) {
                throw new RuntimeException("response body format invalid");
            }

            String signature = jsonObject.getString("signature");
            String data = jsonObject.getString("data");
            String msg = jsonObject.getString("msg");
            String respCode = jsonObject.getString("respCode");

            String temp = null;
            if (null != data && null != signature) {
                temp = AesUtil.decryptToString(data, aesKey);
                if (temp == null || !StacsECKey.verify(temp, signature, pubKey)) {
                    log.error("[decrypt]signature verify failed！");
                    throw new RuntimeException("signature verify failed!");
                }
            }

            if (null == data) {
                response.setData(new JSONObject());
            } else {
                response.setData(temp);
            }
            response.setMsg(msg);
            response.setRespCode(respCode);
        } catch (Exception e) {
            log.error("[decrypt] has error ", e);
            throw new RuntimeException("decrypt has error", e);
        }

        return response;
    }

}
