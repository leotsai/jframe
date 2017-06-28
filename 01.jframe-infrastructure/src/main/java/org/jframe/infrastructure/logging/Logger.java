package org.jframe.infrastructure.logging;

/**
 * Created by leo on 2017-06-28.
 */
public interface Logger {
    void entry(String group, String message);
    void start();
    void stop();
}
