package com.higgschain.trust.drs.model.callback;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyu
 * @description
 * @date 2019-09-04
 */
@Getter
@Setter
public class ActionResult{
    /**
     * the action index
     */
    private int index;
    /**
     * the result-data of action executed
     */
    private JSONObject data;
}
