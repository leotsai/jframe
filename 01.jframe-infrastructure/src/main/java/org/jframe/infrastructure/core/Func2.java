package org.jframe.infrastructure.core;

/**
 * Created by Leo on 2017/1/9.
 */
@FunctionalInterface
public interface Func2<T, Boolean> {
    boolean invoke(T item);
}
