package io.stacs.nav.drs.service.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liuyu
 * @description
 * @date 2019-12-12
 */
@Getter @Setter @ToString public class BDVO {
    private String functionName;
    private JSONObject param;
}
