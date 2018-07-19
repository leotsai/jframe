package org.jframe.infrastructure.cache;

import org.jframe.core.app.AppInitializer;
import org.jframe.core.cache.Cache;

/**
 * DESC:管理缓存
 *
 * @author xiaojin
 * @date 2018-07-16 10:08
 */
public class CacheManager implements AppInitializer {

    private static final CacheManager instance = new CacheManager();

    private Cache cache;


    public static CacheManager getInstance() {
        return instance;
    }

    public Cache getCache() {
        return cache;
    }

    public CacheManager setCache(Cache cache) {
        this.cache = cache;
        return this;
    }

    @Override
    public String init() {
        return this.getClass().getName() + " initialize success!";
    }

    @Override
    public void close() {

    }
}
