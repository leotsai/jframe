package org.jframe.infrastructure.extensions;

/**
 * Created by Leo on 2017/1/5.
 */
@FunctionalInterface
public interface Func<TResult> {
    TResult invoke() throws Exception;
}
