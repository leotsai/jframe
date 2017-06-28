package org.jframe.infrastructure.core;

/**
 * Created by screw on 2017/5/19.
 */
@FunctionalInterface
public interface SafeFunc2<T1, T2, TResult> {
    TResult invoke(T1 item1, T2 item2);
}
