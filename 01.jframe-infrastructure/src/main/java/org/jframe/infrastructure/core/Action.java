package org.jframe.infrastructure.core;

/**
 * Created by Leo on 2017/1/9.
 */
@FunctionalInterface
public interface Action {
    void invoke() throws Exception;
}
