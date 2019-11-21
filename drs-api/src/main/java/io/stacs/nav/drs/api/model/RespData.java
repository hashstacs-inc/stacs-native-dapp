package io.stacs.nav.drs.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author liuyu
 * @description
 * @date 2019-11-05
 */
@Getter @Setter @NoArgsConstructor public class RespData<T> implements java.io.Serializable {
    private final static String SUCCESS_CODE = "000000";
    private final static String SUCCESS_MSG = "SUCCESS";
    private String code = SUCCESS_CODE;
    private String msg = SUCCESS_MSG;
    private T data;

    public RespData(T t) {
        this.data = t;
    }

    public RespData(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public boolean isSuccessful(){
        return SUCCESS_CODE.equals(code);
    }
    public static <T> RespData success(T t) {
        return new RespData(t);
    }

    public static RespData success() {
        return new RespData();
    }

    public static RespData fail(String code, String msg) {
        return new RespData(code, msg);
    }
}
