package org.jframe.infrastructure.extensions;

/**
 * Created by screw on 2017/5/19.
 */
@FunctionalInterface
public interface SafeAction1<T> {
    void invoke(T entity);
}
