package org.jframe.infrastructure.core;

/**
 * Created by Leo on 2017/1/9.
 */
@FunctionalInterface
public interface Func3<T1, T2, Boolean> {
    boolean invoke(T1 item1, T2 item2);
}
