package io.stacs.nav.drs.service.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

/**
 * @author liuyu
 * @description
 * @date 2019-12-12
 */
@Getter @Setter @ToString public class BDVO {

    @NotEmpty(message = "functionName can't be empty")
    private String functionName;

    private JSONObject param;

    @NotEmpty(message = "privateKey can't be empty")
    private String privateKey;
}
