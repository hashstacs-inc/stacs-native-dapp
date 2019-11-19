package com.higgschain.trust.drs.service.utils;

import java.util.Objects;
import java.util.function.*;

/**
 * @author dekuofa <br>
 * @date 2019-11-11 <br>
 * @see <a href="https://stackoverflow.com/a/27644392/10362153"/>
 */
public class LambdaExceptionUtil {
    /**
     * .forEach(rethrowConsumer(name -> System.out.println(Class.forName(name)))); or .forEach(rethrowConsumer(ClassNameUtil::println));
     */
    public static <T, E extends Exception> Consumer<T> rethrowConsumer(ConsumerWithExceptions<T, E> consumer) throws E {
        return t -> {
            try {
                consumer.accept(t);
            } catch (Exception exception) {
                throwAsUnchecked(exception);
            }
        };
    }

    public static <T, U, E extends Exception> BiConsumer<T, U> rethrowBiConsumer(
        BiConsumerWithExceptions<T, U, E> biConsumer) throws E {
        return (t, u) -> {
            try {
                biConsumer.accept(t, u);
            } catch (Exception exception) {
                throwAsUnchecked(exception);
            }
        };
    }

    /**
     * .map(rethrowFunction(name -> Class.forName(name))) or .map(rethrowFunction(Class::forName))
     */
    public static <T, R, E extends Exception> Function<T, R> rethrowFunction(FunctionWithExceptions<T, R, E> function)
        throws E {
        return t -> {
            try {
                return function.apply(t);
            } catch (Exception exception) {
                throwAsUnchecked(exception);
                return null;
            }
        };
    }

    public static <T, U, R, E extends Exception> BiFunction<T, U, R> rethrowFunction(
        BiFunctionWithExceptions<T, U, R, E> function) throws E {
        return (t, u) -> {
            try {
                return function.apply(t, u);
            } catch (Exception exception) {
                throwAsUnchecked(exception);
                return null;
            }
        };
    }

    /**
     * rethrowSupplier(() -> new StringJoiner(new String(new byte[]{77, 97, 114, 107}, "UTF-8"))),
     */
    public static <T, E extends Exception> Supplier<T> rethrowSupplier(SupplierWithExceptions<T, E> function) throws E {
        return () -> {
            try {
                return function.get();
            } catch (Exception exception) {
                throwAsUnchecked(exception);
                return null;
            }
        };
    }

    /**
     * uncheck(() -> Class.forName("xxx"));
     */
    public static void uncheck(RunnableWithExceptions t) {
        try {
            t.run();
        } catch (Exception exception) {
            throwAsUnchecked(exception);
        }
    }

    /**
     * uncheck(() -> Class.forName("xxx"));
     */
    public static <R, E extends Exception> R uncheck(SupplierWithExceptions<R, E> supplier) {
        try {
            return supplier.get();
        } catch (Exception exception) {
            throwAsUnchecked(exception);
            return null;
        }
    }

    /**
     * uncheck(Class::forName, "xxx");
     */
    public static <T, R, E extends Exception> R uncheck(FunctionWithExceptions<T, R, E> function, T t) {
        try {
            return function.apply(t);
        } catch (Exception exception) {
            throwAsUnchecked(exception);
            return null;
        }
    }

    @SuppressWarnings("unchecked") private static <E extends Throwable> void throwAsUnchecked(Exception exception)
        throws E {
        throw (E)exception;
    }

    @FunctionalInterface public interface ConsumerWithExceptions<T, E extends Exception> {
        void accept(T t) throws E;
    }

    @FunctionalInterface public interface BiConsumerWithExceptions<T, U, E extends Exception> {
        void accept(T t, U u) throws E;
    }

    @FunctionalInterface public interface FunctionWithExceptions<T, R, E extends Exception> {
        R apply(T t) throws E;

        default <V> FunctionWithExceptions<T, V, E> andThen(Function<R, V> after) throws E {
            Objects.requireNonNull(after);
            return (T t) -> after.apply(rethrowFunction(this).apply(t));
        }

        default ConsumerWithExceptions<T, E> consumer(Consumer<R> after) throws E {
            Objects.requireNonNull(after);
            return (T t) -> after.accept(rethrowFunction(this).apply(t));
        }
    }

    @FunctionalInterface public interface BiFunctionWithExceptions<T, U, R, E extends Exception> {

        R apply(T t, U u) throws E;

        default <V> BiFunctionWithExceptions<T, U, V, E> andThen(Function<R, V> after) throws E {
            Objects.requireNonNull(after);
            return (T t, U u) -> after.apply(rethrowFunction(this).apply(t, u));
        }

        default BiConsumerWithExceptions<T, U, E> consumer(Consumer<R> after) throws E {
            Objects.requireNonNull(after);
            return (T t, U u) -> after.accept(rethrowFunction(this).apply(t, u));
        }
    }

    @FunctionalInterface public interface SupplierWithExceptions<T, E extends Exception> {
        T get() throws E;
    }

    @FunctionalInterface public interface RunnableWithExceptions<E extends Exception> {
        void run() throws E;
    }

}
