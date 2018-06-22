package org.jframe.core.extensions;

/**
 * Created by leo on 2017-10-09.
 */
@FunctionalInterface
public interface ThrowableFunction<T, TResult> {

    TResult apply(T item) throws Throwable;
}
