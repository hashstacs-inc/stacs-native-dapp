package io.stacs.nav.drs.boot;

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
    }
}
