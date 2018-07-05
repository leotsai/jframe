package org.jframe.infrastructure.redis;

import org.jframe.core.app.AppInitializer;
import org.jframe.core.redis.RedisPoolContext;
import org.jframe.infrastructure.AppContext;

/**
 * Created by leo on 2017-10-22.
 */
public class JframeRedisPoolContext extends RedisPoolContext implements AppInitializer {

    private final static JframeRedisPoolContext instance = new JframeRedisPoolContext();

    public static JframeRedisPoolContext getInstance() {
        return instance;
    }

    private JframeRedisPoolContext() {

    }

    @Override
    public String init() {
        super.initialize(AppContext.getRedisConfig());
        return this.getClass().getName() + " initialize success!";
    }

}
