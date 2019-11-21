package io.stacs.nav.drs.service.scheduler;

import com.lmax.disruptor.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author suimi
 * @date 2019/8/15
 */
@Slf4j public final class LogExceptionHandler<T> implements ExceptionHandler<T> {

    public interface OnEventException<T> {

        void onException(T event, Throwable ex);
    }

    private final String name;
    private final OnEventException<T> onEventException;

    public LogExceptionHandler(String name) {
        this(name, null);
    }

    public LogExceptionHandler(String name, OnEventException<T> onEventException) {
        this.name = name;
        this.onEventException = onEventException;
    }

    @Override public void handleOnStartException(Throwable ex) {
        log.error("Fail to start {} disruptor", this.name, ex);
    }

    @Override public void handleOnShutdownException(Throwable ex) {
        log.error("Fail to shutdown {}r disruptor", this.name, ex);

    }

    @Override public void handleEventException(Throwable ex, long sequence, T event) {
        log.error("Handle {} disruptor event error, event is {}", this.name, event, ex);
        if (this.onEventException != null) {
            this.onEventException.onException(event, ex);
        }
    }
}