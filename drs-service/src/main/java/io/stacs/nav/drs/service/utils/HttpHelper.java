package io.stacs.nav.drs.service.utils;

import com.google.common.base.Joiner;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;

/**
 * @author dekuofa <br>
 * @date 2019-11-21 <br>
 */
public class HttpHelper {

    @Nonnull public static String buildGetRequestParam(List<Pair<String, String>> params) {
        if (CollectionUtils.isEmpty(params))
            return "";
        Joiner joiner = Joiner.on('&');
        Joiner joiner2 = Joiner.on('=');
        Iterator iterator = params.stream().map(pair -> joiner2.join(pair.getLeft(), pair.getRight())).iterator();
        return joiner.join(iterator);
    }
}
