package org.jframe.infrastructure.cache;

import org.apache.commons.lang3.StringUtils;
import org.jframe.core.cache.Cache;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DESC:基于内存的缓存
 *
 * @author xiaojin
 * @date 2018-07-16 10:16
 */
public class MemoryCache implements Cache {

    private static ConcurrentHashMap<String, CacheDto> cacheMap = new ConcurrentHashMap<>();

    @Override
    public void set(String key, String value) {
        cacheMap.put(key, new CacheDto(key, value));
        checkActive();
    }

    @Override
    public void setex(String key, String value, int expireSeconds) {
        cacheMap.put(key, new CacheDto(key, value, expireSeconds));
        checkActive();
    }

    @Override
    public String get(String key) {
        checkActive();
        CacheDto cacheDto = cacheMap.get(key);
        if (cacheDto == null) {
            return "";
        }
        return cacheDto.getValue();
    }

    @Override
    public void del(String key) {
        if (cacheMap.containsKey(key)) {
            cacheMap.remove(key);
        }
    }

    @Override
    public boolean exists(String key) {
        String value = get(key);
        if (StringUtils.isNotBlank(value)) {
            return true;
        }
        return false;
    }

    @Override
    public void incr(String key) {
        String value = get(key);
        if (StringUtils.isNotBlank(value)) {
            set(key, "0");
        } else {
            int num = Integer.parseInt(value);
            num++;
            set(key, String.valueOf(num));
        }
    }

    @Override
    public void expire(String key, int expireSeconds) {
        String value = get(key);
        if (StringUtils.isNotBlank(value)) {
            setex(key, value, expireSeconds);
        }
    }

    private void checkActive() {
        Iterator<String> iterator = cacheMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            CacheDto cacheDto = cacheMap.get(key);
            if (cacheDto.getExpireTime() < System.currentTimeMillis()) {
                iterator.remove();
            }
        }
    }

}
