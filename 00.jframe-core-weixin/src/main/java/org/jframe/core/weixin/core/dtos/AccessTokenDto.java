package org.jframe.core.weixin.core.dtos;

/**
 * Created by leo on 2017-09-23.
 */
public class AccessTokenDto extends _ApiDtoBase {
    private String access_token;
    private int expires_in;//seconds
    private long createdTime;

    public AccessTokenDto(){
        this.createdTime = System.currentTimeMillis();
    }

    public boolean isExpiringOrExpired() {
        long expire = this.createdTime + (this.expires_in-300) * 1000;
        return expire < System.currentTimeMillis();
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public long getCreatedTime() {
        return this.createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }
}
