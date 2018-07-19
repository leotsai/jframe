package org.jframe.infrastructure.cache;

import org.jframe.core.cache.Cache;
import org.jframe.infrastructure.redis.JframeRedisSession;

/**
 * DESC:基于redis的缓存
 *
 * @author xiaojin
 * @date 2018-07-16 10:43
 */
public class RedisCache implements Cache {

    @Override
    public void set(String key, String value) {
        try (JframeRedisSession session = new JframeRedisSession()) {
            session.set(key, value);
        }
    }

    @Override
    public void setex(String key, String value, int expireSeconds) {
        try (JframeRedisSession session = new JframeRedisSession()) {
            session.setex(key, expireSeconds, value);
        }
    }

    @Override
    public String get(String key) {
        try (JframeRedisSession session = new JframeRedisSession()) {
            return session.get(key);
        }
    }

    @Override
    public void del(String key) {
        try (JframeRedisSession session = new JframeRedisSession()) {
            session.del(key);
        }
    }

    @Override
    public boolean exists(String key) {
        try (JframeRedisSession session = new JframeRedisSession()) {
            return session.exists(key);
        }
    }

    @Override
    public void incr(String key) {
        try (JframeRedisSession session = new JframeRedisSession()) {
            session.incr(key);
        }
    }

    @Override
    public void expire(String key, int expireSeconds) {
        try (JframeRedisSession session = new JframeRedisSession()) {
            session.expire(key, expireSeconds);
        }
    }
}
