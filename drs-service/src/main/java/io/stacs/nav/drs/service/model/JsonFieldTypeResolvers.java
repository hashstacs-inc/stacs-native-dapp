package io.stacs.nav.drs.service.model;

import com.alibaba.fastjson.parser.deserializer.FieldTypeResolver;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author dekuofa <br>
 * @date 2019-12-09 <br>
 */
public interface JsonFieldTypeResolvers {

    BiFunction<Predicate<String>, Supplier<Type>, Function<String, Type>> fieldNameResolverBuilder =
        (p, supplier) -> fieldName -> p.test(fieldName) ? supplier.get() : null;

    FieldTypeResolver dateFieldResolver = (object, fieldName) -> fieldNameResolverBuilder.apply(
        name -> name.endsWith("Time"), () -> Date.class).apply(fieldName);

}
