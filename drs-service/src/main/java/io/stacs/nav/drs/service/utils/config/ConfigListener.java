package io.stacs.nav.drs.service.utils.config;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

/**
 * @author dekuofa <br>
 * @date 2019-11-18 <br>
 */
public interface ConfigListener {

    <T> void updateNotify(T config);

    @Nonnull default Predicate<Object> filter() {
        // no filter
        return obj -> true;
    }

}
