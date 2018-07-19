package org.jframe.core.redis;

import org.jframe.core.extensions.Action;
import org.jframe.core.extensions.Function0;
import org.jframe.core.logging.LogHelper;
import redis.clients.jedis.Jedis;

/**
 * Created by Leo on 2017/11/3.
 */
public class RedisSession implements AutoCloseable {

    private Jedis jedis;
    private boolean ignoreAllExceptions;

    protected RedisSession(RedisPoolContext poolContext) {
        this(poolContext, true);
    }

    protected RedisSession(RedisPoolContext poolContext, boolean ignoreAllExceptions) {
        this.ignoreAllExceptions = ignoreAllExceptions;
        try {
            this.jedis = poolContext.getResource();
        } catch (Exception ex) {
            LogHelper.log("redisex0", ex);
            if (!this.ignoreAllExceptions) {
                throw ex;
            }
        }
    }

    private <T> T tryGet(Function0<T> getter) {
        if (this.jedis == null) {
            return null;
        }
        try {
            return getter.apply();
        } catch (Exception ex) {
            LogHelper.log("redisex1", ex);
            if (!this.ignoreAllExceptions) {
                throw ex;
            }
        }
        return null;
    }

    private void tryDo(Action action) {
        if (this.jedis == null) {
            return;
        }
        try {
            action.apply();
        } catch (Exception ex) {
            LogHelper.log("redisex2", ex);
            if (!this.ignoreAllExceptions) {
                throw ex;
            }
        }
    }

    public String get(String key) {
        return this.tryGet(() -> this.jedis.get(this.getActualKey(key)));
    }

    public boolean exists(String key) {
        return this.tryGet(() -> this.jedis.exists(this.getActualKey(key)));
    }

    public void expire(String key, int expireSeconds) {
        this.tryDo(() -> this.jedis.expire(this.getActualKey(key), expireSeconds));
    }

    public void set(String key, String value) {
        this.tryDo(() -> this.jedis.set(this.getActualKey(key), value));
    }

    public void setex(String key, int expireSeconds, String value) {
        this.tryDo(() -> this.jedis.setex(this.getActualKey(key), expireSeconds, value));
    }

    public void incr(String key) {
        this.tryDo(() -> this.jedis.incr(key));
    }

    public void del(String key) {
        this.tryDo(() -> this.jedis.del(this.getActualKey(key)));
    }

    protected String getActualKey(String key) {
        return key;
    }

    @Override
    public void close() {
        if (this.jedis != null) {
            try {
                this.jedis.close();
            } catch (Exception ex) {
                LogHelper.log("redisex3", ex);
                if (!this.ignoreAllExceptions) {
                    throw ex;
                }
            }
        }
    }
}
