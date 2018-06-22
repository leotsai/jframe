package org.jframe.infrastructure.redis;

import org.jframe.infrastructure.AppContext;
import org.jframe.core.redis.RedisPoolContext;

/**
 * Created by leo on 2017-10-22.
 */
public class RedisWpkPoolContext extends RedisPoolContext {

    private final static RedisWpkPoolContext instance = new RedisWpkPoolContext();

    public static RedisWpkPoolContext getInstance(){
        return instance;
    }

    private RedisWpkPoolContext(){

    }

    public void initialize(){
        super.initialize(AppContext.getRedisConfig());
    }

}
