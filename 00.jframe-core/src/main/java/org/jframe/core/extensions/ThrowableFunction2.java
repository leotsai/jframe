package org.jframe.core.extensions;

/**
 * Created by Leo on 2017/1/9.
 */
@FunctionalInterface
public interface ThrowableFunction2<T1, T2, TResult> {

    TResult apply(T1 item1, T2 item2) throws Throwable;
}
