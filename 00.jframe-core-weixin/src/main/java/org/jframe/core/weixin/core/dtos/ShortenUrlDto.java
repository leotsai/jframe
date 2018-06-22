package org.jframe.core.weixin.core.dtos;

/**
 * Created by leo on 2017-09-24.
 */
public class ShortenUrlDto extends _ApiDtoBase {
    private String short_url;

    public String getShort_url() {
        return short_url;
    }

    public void setShort_url(String short_url) {
        this.short_url = short_url;
    }
}
