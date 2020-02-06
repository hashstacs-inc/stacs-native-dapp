package io.stacs.nav.drs.boot;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

/**
 * @author tangkun
 * @date 2019-12-25
 */
@Slf4j
public class AppTest {

    @Test
    public void testOptional(){
        System.out.println("222222222:"+Optional.ofNullable(null).map(o -> {
          System.out.println("xxxxxxxxxxxxx");
          return o;
      }).orElse("0"));

        JSONArray arr = JSONArray.parseArray("[{\"args\":[true],\"currency\":null,\"from\":\"323c1e309841d2feb683b1227658de77d90406bf\",\"index\":0,\"methodSignature\":\"(bool) changeFroze(bool)\",\"signature\":null,\"to\":\"3900e2930f5470923ec96f6e3c0a63212d79763a\",\"type\":\"CONTRACT_INVOKED\",\"value\":null,\"version\":\"1.0.0\"}]");
        JSONObject o = arr.getJSONObject(0);
        System.out.println(o.getString("to"));
    }
}
