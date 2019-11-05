package com.higgschain.trust.drs.boot.exception;

import lombok.Getter;

/**
 * @author liuyu
 * @description
 * @date 2019-11-05
 */
@Getter public enum DappError {
    DAPP_COMMON_ERROR("100000", "unknown error"),
    DAPP_ALREADY_EXISTS("100001", "Dapp is already exists"),
    DAPP_NOT_EXISTS("100002", "Dapp is not exists"),
    DAPP_NOT_INITIALIZED("100003", "Dapp is not initialized"),
    DAPP_ALREADY_RUNNING("100004", "Dapp is already running"),
    DAPP_UPDATE_STATUS_ERROR("100005", "udpate Dapp status is error"),
    DAPP_SOURCE_FILE_NOT_EXISTS_ERROR("100006", "source file is not exists"),
    DB_ERROR("100007", "has data base error" ),
    IDEMPOTENT_ERROR("100008", "has idempotent error" )

    ;

    private String code;
    private String msg;

    DappError(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
