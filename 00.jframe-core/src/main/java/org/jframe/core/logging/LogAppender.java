package org.jframe.core.logging;

/**
 * Created by leo on 2017-06-28.
 */
public interface LogAppender {
    void entry(String group, String message);
    void start();
    void stop();
    boolean isStarted();
    boolean printStackTrace();
    String getServerName();
    boolean autoAppendHttpHeaders();
}
