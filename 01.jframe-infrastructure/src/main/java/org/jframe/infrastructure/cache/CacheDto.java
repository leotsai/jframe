package org.jframe.infrastructure.cache;

/**
 * DESC:
 *
 * @author xiaojin
 * @date 2018-07-16 10:20
 */
public class CacheDto {

    private String key;
    private String value;
    private long expireTime;

    public CacheDto(String key, String value) {
        this.key = key;
        this.value = value;
        this.expireTime = -1L;
    }

    public CacheDto(String key, String value, long expireTime) {
        this.key = key;
        this.value = value;
        this.expireTime = expireTime * 1000 + System.currentTimeMillis();
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public long getExpireTime() {
        return expireTime;
    }
}
