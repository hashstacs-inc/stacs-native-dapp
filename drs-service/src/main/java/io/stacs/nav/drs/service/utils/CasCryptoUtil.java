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
 * @desc 钱包与Cas Realm对接所需的加密解密接口工具类
 * @desc 钱包与Cas Realm维护同一套公私钥
 * @date 2018/2/14 14:52
 */
@Slf4j public class CasCryptoUtil {

    public final static int JSON_GENERATE_FEATURES;
    private static final BaseEncoding HEX = BaseEncoding.base16().lowerCase();

    static {
        int features = 0;
        //JSON不做循环引用检测
        features |= SerializerFeature.DisableCircularReferenceDetect.getMask();
        //JSON输出NULL属性
        features |= SerializerFeature.WriteMapNullValue.getMask();
        //toJSONString的时候对一级key进行按照字母排序
        features |= SerializerFeature.SortField.getMask();
        //toJSONString的时候对嵌套结果进行按照字母排序
        features |= SerializerFeature.MapSortField.getMask();
        JSON_GENERATE_FEATURES = features;
    }

    /**
     * @param message 需要加密的对象
     * @param priKey  发起方的私钥
     * @param aesKey  AES秘钥(16bytes长度)
     * @return CasEncryptRequest  包含requestParam（加密后）和signature字段
     * @desc 加密方法    先使用发起方的私钥签名   再使用AES加密requestParam
     */
    public static CasEncryptRequest encrypt(Object message, String priKey, String aesKey) {

        // 空校验
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

        // 使用发起方私钥对requestParam转换成的json字符串进行签名
        StacsECKey priEcKey = StacsECKey.fromPrivate(HEX.decode(priKey));

        String originMsg =
            message instanceof String ? (String)message : JSON.toJSONString(message, JSON_GENERATE_FEATURES);

        if (log.isDebugEnabled()) {
            log.debug("request origin message:{}", originMsg);
        }
        casEncryptRequest.setSignature(priEcKey.signMessage(originMsg));
        String encryptString = AesUtil.encryptToString(originMsg, aesKey);
        // 使用AES加密requestParam
        casEncryptRequest.setRequestParam(encryptString);

        return casEncryptRequest;
    }

    /**
     * @param resp   请求返回的String对象
     * @param pubKey 发起方的私钥
     * @param aesKey aes解密需要的key
     * @return CasDecryptReponse  解密 验签完成后返回的对象
     * @desc 接收http请求并进行解密操作
     */
    public static CasDecryptResponse decrypt(String resp, String pubKey, String aesKey) {
        CasDecryptResponse response = new CasDecryptResponse();

        try {
            JSONObject jsonObject = JSON.parseObject(resp);
            if (!jsonObject.containsKey("data") || !jsonObject.containsKey("signature") || !jsonObject
                .containsKey("respCode") || !jsonObject.containsKey("msg")) {
                throw new RuntimeException("response body format invalid");
            }

            // 对data进行aes解密操作
            // 对signature进行验证签名操作
            String signature = jsonObject.getString("signature");
            String data = jsonObject.getString("data");
            String msg = jsonObject.getString("msg");
            String respCode = jsonObject.getString("respCode");

            String temp = null;
            if (null != data && null != signature) {
                temp = AesUtil.decryptToString(data, aesKey);
                if (temp == null || !StacsECKey.verify(temp, signature, pubKey)) {
                    // 验证签名失败
                    log.error("[decrypt]signature verify failed！");
                    throw new RuntimeException("signature verify failed!");
                }
            }

            // 组装返回数据
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
