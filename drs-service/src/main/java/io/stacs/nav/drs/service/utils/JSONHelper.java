package io.stacs.nav.drs.service.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

/**
 * @author dekuofa <br>
 * @date 2019-12-10 <br>
 */
@Slf4j
public class JSONHelper {

    public static <T> Optional<T> parseJSONObject(String json, Class<T> clazz) {
        try {
            return Optional.of(JSONObject.parseObject(json, clazz));
        } catch (JSONException e) {
            return Optional.empty();
        }
    }

    public static <T> Optional<T> toJavaObject(JSONObject json, Class<T> clazz) {
        try {

            return Optional.of(JSON.toJavaObject(json, clazz));
        } catch (JSONException e) {
            log.error("toJavaObject has error,json:{}",json,e);
            return Optional.empty();
        }
    }

    public static <T> Optional<List<T>> toJavaList(JSONArray array, Class<T> clazz) {
        try {
            // JSONArray.parseArray()
            return Optional.of(JSON.parseArray(array.toJSONString(), clazz));
        } catch (JSONException e) {
            return Optional.empty();
        }
    }

    public static Optional<JSONObject> parseJSONObject(String json) {
        try {
            return Optional.of(JSONObject.parseObject(json));
        } catch (JSONException e) {
            return Optional.empty();
        }
    }

    public static Optional<JSONArray> parseJSONOArray(String json) {
        try {
            return Optional.of(JSONArray.parseArray(json));
        } catch (JSONException e) {
            return Optional.empty();
        }
    }
}
