package org.jframe.core.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import org.jframe.core.helpers.StringHelper;

/**
 * Created by leo on 2017-08-22.
 */
public class RedisPoolContext {

    private JedisPool pool;

    public void initialize(RedisConfig config) {
        if (this.pool == null) {
            int timeout = config.getTimeoutSeconds() * 1000;
            if(StringHelper.isNullOrWhitespace(config.getPassword())){
                this.pool = new JedisPool(new JedisPoolConfig(), config.getAddress(), config.getPort(), timeout);
            }
            else{
                this.pool = new JedisPool(new JedisPoolConfig(), config.getAddress(), config.getPort(), timeout, config.getPassword());
            }
            System.out.println("Redis Pool initialized");
        }
    }

    public Jedis getResource() {
        return this.pool.getResource();
    }

    public void close(){
        this.pool.close();
    }

}
