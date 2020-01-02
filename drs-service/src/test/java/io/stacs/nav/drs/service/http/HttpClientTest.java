package io.stacs.nav.drs.service.http;

import io.stacs.nav.drs.api.enums.ApiConstants;
import io.stacs.nav.drs.api.model.bd.BusinessDefine;
import io.stacs.nav.drs.service.network.BlockChainHelper;
import org.junit.Test;

import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

/**
 * @author dekuofa <br>
 * @date 2019-11-20 <br>
 */
public class HttpClientTest {
    private static Predicate<String> encryptFilter = Pattern.compile("[Qq]uery").asPredicate();
    private static final String callBackUrl = "callback/register";

    @Test public void filter_test() {
        String[] passUrls = {"/xxx/batchQuery", "/xxx/query", "/xxx/queryXXXXX",};

        String[] notMatchUrls = {"/xxx/open/xxx", "/xxx/open", "/ca/cancel",};

        assertEquals(passUrls.length, Stream.of(passUrls).filter(encryptFilter).count());
        assertEquals(0, Stream.of(notMatchUrls).filter(encryptFilter).count());
    }

    public static void main(String[] args) {
        try {
            new BlockChainHelper()
                .post((ApiConstants.TransactionApiEnum.BD_PUBLISH).getApi(), new BusinessDefine(), Object.class);
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

}
