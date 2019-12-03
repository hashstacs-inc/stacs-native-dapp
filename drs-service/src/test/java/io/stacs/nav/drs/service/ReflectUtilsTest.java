package io.stacs.nav.drs.service;

import io.stacs.nav.drs.service.utils.ReflectUtils;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

/**
 * @author dekuofa <br>
 * @date 2019-12-02 <br>
 */
public class ReflectUtilsTest {

    private static Function<Object[], String> TEST_FUNCTION = args -> "hello";

    private static Consumer<Object[]> TEST_CONSUMER = args -> {
        String message = (String)(args[0]);
        System.out.println(message);
    };

    private static BiFunction<Method, Object[], Function<Object[], ?>> TEST_MULTI = (method, args) -> {
        if (method.getName().equals("sayHello")) {
            return objs -> {
                TEST_CONSUMER.accept(objs);
                return null;
            };
        } else if (method.getName().equals("getHello")) {
            return TEST_FUNCTION;
        }
        return null;
    };

    // @formatter:off
    private ReflectTestInterface multiTestInstance = ReflectUtils.getInstance(
        ReflectTestInterface.class,
        ReflectUtils.newMultiProxy(TEST_MULTI));

    @Test public void test_reflect_function() {
        ReflectTestInterface service = ReflectUtils.getInstance(
            ReflectTestInterface.class,
            ReflectUtils.newProxy(TEST_FUNCTION, (method,args) -> true));
        assertEquals("hello", service.getHello());

    }

    @Test public void test_reflect_consumer() {
        ReflectTestInterface service = ReflectUtils.getInstance(
            ReflectTestInterface.class,
            ReflectUtils.newProxy(TEST_CONSUMER, (method,args) -> true));
        service.sayHello("hello, world");

    }

    @Test public void test_reflect_Multi() {
        assertEquals("hello", multiTestInstance.getHello());
        multiTestInstance.sayHello("hello, world");

    }
    // @formatter:on

    public interface ReflectTestInterface {
        String getHello();

        void sayHello(String message);
    }
}
