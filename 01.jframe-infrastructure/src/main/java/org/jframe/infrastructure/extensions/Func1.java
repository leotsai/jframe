package org.jframe.infrastructure.extensions;

/**
 * Created by Leo on 2017/1/9.
 */
@FunctionalInterface
public interface Func1<T, TResult> {
    TResult invoke(T item) throws Exception;
}
