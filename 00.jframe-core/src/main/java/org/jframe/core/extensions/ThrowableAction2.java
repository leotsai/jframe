package org.jframe.core.extensions;

/**
 * Created by leo on 2017-10-09.
 */
@FunctionalInterface
public interface ThrowableAction2<T1, T2> {
    void apply(T1 item1, T2 item2) throws Throwable;
}
