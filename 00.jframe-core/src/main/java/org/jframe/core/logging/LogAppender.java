package org.jframe.core.logging;


import org.jframe.core.logging.enums.LogArea;

/**
 * Created by leo on 2017-06-28.
 */
public interface LogAppender {
    void entry(LogArea area, String group, String message);

    void start();

    void stop();

    boolean isStarted();

}
