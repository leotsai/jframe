package org.jframe.core.weixin.core.dtos;

/**
 * Created by leo on 2017-09-24.
 */
public class GetQrCodeDto extends _ApiDtoBase {
    private String ticket;
    private int expire_seconds ;
    private String url ;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getExpire_seconds() {
        return expire_seconds;
    }

    public void setExpire_seconds(int expire_seconds) {
        this.expire_seconds = expire_seconds;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
