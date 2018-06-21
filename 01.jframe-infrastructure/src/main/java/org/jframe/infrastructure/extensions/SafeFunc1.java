package org.jframe.infrastructure.extensions;

/**
 * Created by screw on 2017/5/19.
 */
@FunctionalInterface
public interface SafeFunc1<T, TResult> {
    TResult invoke(T item);
}
