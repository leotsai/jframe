package org.jframe.core.extensions;

/**
 * Created by leo on 2017-10-09.
 */
@FunctionalInterface
public interface ThrowableAction {
    void apply() throws Throwable;
}
