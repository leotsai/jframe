package org.jframe.core.redis;

/**
 * redis缓存
 *
 * @author xiaojin
 */
public interface RedisCacheReader {
    /**
     * 从redis中读取数据
     *
     * @return
     */
    Object read();

    /**
     * 刷新redis中的数据
     */
    void refresh();

    /**
     * 清除redis中的数据
     */
    void clear();
}
