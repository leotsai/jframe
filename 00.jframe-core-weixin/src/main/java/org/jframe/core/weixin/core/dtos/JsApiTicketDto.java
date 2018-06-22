package org.jframe.core.weixin.core.dtos;

/**
 * Created by leo on 2017-09-24.
 */
public class JsApiTicketDto extends _ApiDtoBase {
    private String ticket ;
    private int expires_in;

    private long createdTime;
    public JsApiTicketDto(){
        this.createdTime = System.currentTimeMillis();
    }

    public boolean isExpiringOrExpired() {
        long expire = this.createdTime + (this.expires_in-300) * 1000;
        return expire < System.currentTimeMillis();
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }
}
