package com.higgschain.trust.drs.api.exception;

import lombok.Getter;

/**
 * @author liuyu
 * @description
 * @date 2019-11-05
 */
@Getter public class DappException extends RuntimeException {
    private String code;
    private String msg;
    private DappError error;

    public DappException(DappError error) {
        super(error.getMsg());
        this.error = error;
        this.code = error.getCode();
        this.msg = error.getMsg();
    }

    public DappException(String error) {
        super(error);
        this.code = DappError.DAPP_COMMON_ERROR.getCode();
        this.msg = error;
    }
}
