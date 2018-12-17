package org.jframe.core.logging;

/**
 * Created by leo on 2018-12-11.
 */
public interface LoggingConfig {
    boolean printStackTrace();

    String getServerName();

    boolean appendHttpHeaders();

    int getFlushIntervalSeconds();

    int getInitialDelaySeconds();
}
