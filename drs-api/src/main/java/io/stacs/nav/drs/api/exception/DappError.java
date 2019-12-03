package io.stacs.nav.drs.api.exception;

import lombok.Getter;

/**
 * @author liuyu
 * @description
 * @date 2019-11-05
 */
@Getter public enum DappError implements ErrorInfo {

    // @formatter:off
    DAPP_COMMON_ERROR("20","00","00", "unknown error"),
    DAPP_NET_WORK_COMMON_ERROR("20","00","01", "net work common error"),
    IDEMPOTENT_ERROR("20","00","02", "has idempotent error" ),
    PARAM_VALIDATE_ERROR("20","00","03", "param validate error"),
    DB_ERROR("20","00","04", "has data base error" ),

    DAPP_ALREADY_EXISTS("21","00","01", "Dapp is already exists"),
    DAPP_NOT_EXISTS("21","01","00", "Dapp is not exists"),
    DAPP_NOT_INITIALIZED("21","00","03", "Dapp is not initialized"),
    DAPP_ALREADY_RUNNING("21","00","04", "Dapp is already running"),
    DAPP_UPDATE_STATUS_ERROR("21","00","05", "udpate status is error"),
    DAPP_SOURCE_FILE_NOT_EXISTS_ERROR("21","00","06", "source file is not exists"),
    DAPP_ALREADY_INITIALIZED("21","00","07", "Dapp is already initialized"),
    DAPP_CONFIG_NOT_FOUND("21","00","08", "Dapp config file is not found"),
    DAPP_DRS_VERSION_NOT_EXISTS("21","00","09", "Dapp drs version is not exists"),
    DAPP_VERSION_UNEQUAL("21","00","10", "Dapp is unequal"),

    BD_NOT_FIND_ERROR("22","00","01", "bd is not find error"),
    NO_PERMISSION_ERROR("22","00","02", "no permission error"),
    FUNCTION_NOT_FIND_ERROR("22","00","03", "function not find error"),

    DRS_NET_WORK_COMMON_ERROR("22", "00", "04","net work common error"),
    ;
    // @formatter:on

    private final String majorCode;

    private final String minorCode;

    private final String errorCode;

    private final String message;

    DappError(String majorCode, String minorCode, String errorCode, String message) {
        this.majorCode = majorCode;
        this.minorCode = minorCode;
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override public String getCode() {
        return majorCode + minorCode + errorCode;
    }

    @Override public String getMessage() {
        return message;
    }
}
