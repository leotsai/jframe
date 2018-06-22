package org.jframe.core.extensions;

/**
 * Created by Leo on 2017/1/9.
 */
@FunctionalInterface
public interface ThrowableFunction0<TResult> {
    TResult apply() throws Throwable;
}
