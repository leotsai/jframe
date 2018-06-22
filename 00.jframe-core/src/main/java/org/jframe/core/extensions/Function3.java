package org.jframe.core.extensions;

/**
 * Created by leo on 2017-10-09.
 */
@FunctionalInterface
public interface Function3<T1, T2, T3, TResult> {

    TResult apply(T1 item1, T2 item2, T3 item3);
}
