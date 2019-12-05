package io.stacs.nav.drs.service.utils.config;

import org.springframework.context.annotation.Configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author dekuofa <br>
 * @date 2019-11-18 <br>
 */
@Target(ElementType.TYPE) @Retention(RetentionPolicy.RUNTIME) @Configuration public @interface DynamicConfig {

}
