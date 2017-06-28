package org.jframe.infrastructure.core;

/**
 * Created by Leo on 2017/1/9.
 */
@FunctionalInterface
public interface Func2<T1, T2, TResult> {
    TResult invoke(T1 item1, T2 item2) throws Exception;
}
