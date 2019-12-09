package io.stacs.nav.drs.api.model.bo;

import io.stacs.nav.drs.api.model.BaseBO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.stacs.nav.drs.api.model.bo.SignInfo.SignTypeEnum.BIZ;

/**
 * The type Sign info.
 *
 * @author liuyu
 * @description
 * @date 2018 -06-06
 */
@Getter @Setter public class SignInfo implements BaseBO {
    /**
     * who`s sign,rs-name
     */
    private String owner;
    /**
     * the sign data
     */
    private String sign;
    /**
     * sign type
     */
    private SignTypeEnum signType = BIZ;
    /**
     * the id of domain
     */
    private String domainId;

    /**
     * make map of sign data
     * key:owner
     * value:sign
     *
     * @param signInfos the sign infos
     * @return map
     */
    public static Map<String, SignInfo> makeSignMap(List<SignInfo> signInfos) {
        if (CollectionUtils.isEmpty(signInfos)) {
            return new HashMap<>();
        }
        return signInfos.stream().collect(Collectors.toMap(SignInfo::getOwner, v -> v));
    }

    /**
     * The enum Sign type enum.
     */
    public enum SignTypeEnum {
        /**
         * The Biz.
         */
        BIZ("BIZ", "for business"),
        /**
         * The Consensus.
         */
        CONSENSUS("CONSENSUS", "for consensus"),
        ;
        /**
         * The Code.
         */
        String code;
        /**
         * The Msg.
         */
        String msg;

        SignTypeEnum(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        /**
         * Gets by code.
         *
         * @param code the code
         * @return the by code
         */
        public static SignTypeEnum getByCode(String code) {
            for (SignTypeEnum signType : SignTypeEnum.values()) {
                if (signType.getCode().equals(code)) {
                    return signType;
                }
            }
            return null;
        }

        /**
         * Gets code.
         *
         * @return the code
         */
        public String getCode() {
            return code;
        }

        /**
         * Gets msg.
         *
         * @return the msg
         */
        public String getMsg() {
            return msg;
        }
    }
}
