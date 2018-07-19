package org.jframe.core.cache;

/**
 * DESC:缓存接口
 *
 * @author xiaojin
 * @date 2018-07-16 10:00
 */
public interface Cache {

    /**
     * 设置缓存
     *
     * @param key
     * @param value
     */
    void set(String key, String value);

    /**
     * 设置缓存，并设置过期时间
     *
     * @param key
     * @param value
     * @param expireSeconds 过期时间，单位s（秒）
     */
    void setex(String key, String value, int expireSeconds);


    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 删除key
     *
     * @param key
     */
    void del(String key);

    /**
     * 判断key是否存在
     *
     * @param key
     * @return
     */
    boolean exists(String key);


    /**
     * 自增，如果key不存在，则为零
     *
     * @param key
     */
    void incr(String key);


    /**
     * 设置key的过期时间
     *
     * @param key
     * @param expireSeconds
     */
    void expire(String key, int expireSeconds);

}
