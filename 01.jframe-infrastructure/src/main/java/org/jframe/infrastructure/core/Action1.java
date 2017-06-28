package org.jframe.infrastructure.core;

/**
 * Created by screw on 2017/5/19.
 */
@FunctionalInterface
public interface Action1<T> {
    void invoke(T entity) throws Exception;
}
