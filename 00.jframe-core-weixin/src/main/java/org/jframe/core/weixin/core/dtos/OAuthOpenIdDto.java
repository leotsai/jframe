package org.jframe.core.weixin.core.dtos;

/**
 * Created by leo on 2017-09-24.
 * https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140842
 */
public class OAuthOpenIdDto extends _ApiDtoBase {
    private String access_token ;
    private int expires_in;//access_token接口调用凭证超时时间，单位（秒）
    private String refresh_token ;
    private String openid ;
    private String scope ;

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

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
