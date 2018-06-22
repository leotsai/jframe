package org.jframe.core.redis;

/**
 * Created by leo on 2017-10-21.
 */
public interface RedisConfig {
    String getAddress();
    int getPort();
    int getTimeoutSeconds();
    String getPassword();
}
