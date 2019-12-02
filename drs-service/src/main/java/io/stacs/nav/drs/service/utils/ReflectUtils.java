package io.stacs.nav.drs.service.utils;

import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author dekuofa <br>
 * @date 2019-12-02 <br>
 */
public class ReflectUtils {

    public static <T> MethodProxy<T> newProxy(Function<Object[], T> runner, BiPredicate<Method, Object[]> chooser) {
        return new MethodProxy<>(runner, chooser);
    }

    public static MethodProxy<Void> newProxy(Consumer<Object[]> runner, BiPredicate<Method, Object[]> chooser) {
        return new MethodProxy<>(runner, chooser);
    }

    public static MultiMethodProxy newMultiProxy(BiFunction<Method, Object[], Function<Object[], ?>> runnerFunction) {
        return new MultiMethodProxy(runnerFunction);
    }

    // @formatter:off
    @SuppressWarnings("unchecked") public static <T> T getInstance(Class<T> clazz, InvocationHandler proxy) {
        Object newProxyInstance = Proxy.newProxyInstance(
            clazz.getClassLoader(),
            new Class[] {clazz},
            proxy);
        return (T)newProxyInstance;
    }

    public static class MethodProxy<T> implements InvocationHandler {

        private BiPredicate<Method, Object[]> chooser;
        private Function<Object[], T> runner;

        private MethodProxy() {
        }

        public MethodProxy(Function<Object[], T> runner, BiPredicate<Method, Object[]> chooser) {
            Objects.requireNonNull(runner);
            Objects.requireNonNull(chooser);
            this.runner = runner;
            this.chooser = chooser;
        }

        public MethodProxy(Consumer<Object[]> runner, BiPredicate<Method, Object[]> chooser) {
            Objects.requireNonNull(runner);
            Objects.requireNonNull(chooser);
            this.runner = args -> {
                runner.accept(args);
                return null;
            };
            this.chooser = chooser;
        }

        @Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(this, args);
            } else if (chooser.test(method, args)) {
                return runner.apply(args);
            }
            throw new NoSuchMethodException(
                String.format("%s do not have method: %s", proxy.getClass().getName(), method.getName()));
        }

    }

    public static class MultiMethodProxy implements InvocationHandler {

        private BiFunction<Method, Object[], Function<Object[], ?>> runnerFunction;

        private MultiMethodProxy() {
        }

        public MultiMethodProxy(BiFunction<Method, Object[], Function<Object[], ?>> runnerFunction) {
            Objects.requireNonNull(runnerFunction);
            this.runnerFunction = runnerFunction;
        }

        @Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(this, args);
            } else {
                Function<Object[], ?> runner = runnerFunction.apply(method, args);
                if (runner == null) {
                    throw new NoSuchMethodException(
                        String.format("%s do not have method: %s", proxy.getClass().getName(), method.getName()));
                }
                return runner.apply(args);
            }
        }

    }

    // @formatter:on
}
