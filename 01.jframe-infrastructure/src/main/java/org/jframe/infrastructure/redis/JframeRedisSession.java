package org.jframe.infrastructure.redis;

import org.jframe.core.redis.RedisSession;
import org.jframe.infrastructure.AppContext;

/**
 * Created by Leo on 2017/11/3.
 */
public class JframeRedisSession extends RedisSession {

    public JframeRedisSession() {
        super(JframeRedisPoolContext.getInstance());
    }

    @Override
    protected String getActualKey(String key) {
        return AppContext.getAppConfig().getRedisKeyPrefix() + key;
    }


}
