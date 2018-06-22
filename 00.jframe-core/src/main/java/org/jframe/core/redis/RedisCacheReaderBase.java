package org.jframe.core.redis;

import org.jframe.core.helpers.JsonHelper;
import org.jframe.core.logging.LogHelper;

/**
 * Created by leo on 2017-10-01.
 */
abstract class RedisCacheReaderBase<T> implements RedisCacheReader {

    private final Class<T> clazz;

    protected Integer expireSeconds = null;

    protected abstract Object readFromDb();

    protected abstract String getCacheKey();

    protected abstract RedisSession getSession();

    public RedisCacheReaderBase(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void refresh() {
        String json = JsonHelper.serialize(this.readFromDb());
        try (RedisSession session = this.getSession()) {
            this.setCache(session, json);
        }
    }

    @Override
    public void clear() {
        try (RedisSession session = this.getSession()) {
            session.del(this.getCacheKey());
        }
    }

    protected Class<T> getClazz() {
        return this.clazz;
    }

    protected void setCache(RedisSession session, String value) {
        if (this.expireSeconds == null) {
            session.set(this.getCacheKey(), value);
        } else {
            session.setex(this.getCacheKey(), this.getExpireSeconds(), value);
        }

    }

    protected String getRedisValue() {
        try (RedisSession session = this.getSession()) {
            return session.get(this.getCacheKey());
        } catch (Exception ex) {
            LogHelper.log(this.getClazz() + ".getRedisValue", ex);
            return null;
        }
    }

    protected Object getFromDbAndSetCache() {
        Object value = this.readFromDb();
        String json = JsonHelper.serialize(value);
        try (RedisSession session = this.getSession()) {
            this.setCache(session, json);
        } catch (Exception ex) {
            LogHelper.log(this.getClazz() + ".getFromDbAndSetCache", ex);
        }
        return value;
    }

    public abstract Integer getExpireSeconds();
}
