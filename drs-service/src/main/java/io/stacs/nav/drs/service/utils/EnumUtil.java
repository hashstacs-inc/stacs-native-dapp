package io.stacs.nav.drs.service.utils;

import javax.annotation.Nullable;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * @author ganxiang
 * @date 10/30/0030
 */
public class EnumUtil {

    @Nullable public static <E, P> E getEnum(E[] enums, P param, BiPredicate<E, P> predicate) {
        for (E e : enums) {
            if (predicate.test(e, param)) {
                return e;
            }
        }
        return null;
    }

    @Nullable public static <E, P> E getEnum(E[] enums, P param, Function<E, P> getParam) {
        for (E e : enums) {
            if (getParam.apply(e).equals(param)) {
                return e;
            }
        }
        return null;
    }

}
