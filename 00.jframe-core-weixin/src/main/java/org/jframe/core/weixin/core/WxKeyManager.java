package org.jframe.core.weixin.core;

import org.jframe.core.app.AppInitializer;
import org.jframe.core.extensions.KnownException;
import org.jframe.core.weixin.AppletApi;
import org.jframe.core.weixin.WeixinApi;
import org.jframe.core.weixin.core.dtos.AccessTokenDto;
import org.jframe.core.weixin.core.dtos.JsApiTicketDto;

/**
 * Created by leo on 2017-09-23.
 */
public class WxKeyManager implements AppInitializer {

    private final static WxKeyManager instance = new WxKeyManager();

    public static WxKeyManager getInstance() {
        return instance;
    }

    private WxCacheProvider cacheProvider;

    private WxKeyManager() {
    }

    public WxKeyManager setCacheProvider(WxCacheProvider cacheProvider) {
        this.cacheProvider = cacheProvider;
        return this;
    }

    @Override
    public void initialize() {
        if (this.cacheProvider == null) {
            throw new KnownException(this.getClass().getName() + " cacheProvider is null");
        }
    }

    public String getAccessToken(WeixinApi api) {
        synchronized (api.getAppId().intern()) {
            AccessTokenDto tokenDto = this.cacheProvider.getAccessToken(api.getAppId());
            if (tokenDto != null && !tokenDto.isExpiringOrExpired()) {
                return tokenDto.getAccess_token();
            }
            return this.refreshAccessTokenByApi(api);
        }
    }

    public String getAccessToken(AppletApi api) {
        synchronized (api.getAppId().intern()) {
            AccessTokenDto tokenDto = this.cacheProvider.getAccessToken(api.getAppId());
            if (tokenDto != null && !tokenDto.isExpiringOrExpired()) {
                return tokenDto.getAccess_token();
            }
            return this.refreshAccessTokenByApi(api);
        }
    }

    public String getJsApiTicket(WeixinApi api) {
        synchronized (api.getAppId().intern()) {
            JsApiTicketDto ticketDto = this.cacheProvider.getJsApiTicket(api.getAppId());
            if (ticketDto != null && !ticketDto.isExpiringOrExpired()) {
                return ticketDto.getTicket();
            }
            return this.refreshJsApiTicketByApi(api);
        }
    }

    public String refreshAccessTokenByApi(WeixinApi api) {
        AccessTokenDto tokenDto = api.getAccessTokenByApi();
        if (tokenDto == null || !tokenDto.isSuccess()) {
            return null;
        }
        this.cacheProvider.setAccessToken(api.getAppId(), tokenDto);
        return tokenDto.getAccess_token();
    }

    public String refreshAccessTokenByApi(AppletApi api) {
        AccessTokenDto tokenDto = api.getAccessTokenByApi();
        if (tokenDto == null || !tokenDto.isSuccess()) {
            return null;
        }
        this.cacheProvider.setAccessToken(api.getAppId(), tokenDto);
        return tokenDto.getAccess_token();
    }

    public String refreshJsApiTicketByApi(WeixinApi api) {
        JsApiTicketDto ticketDto = api.getJsApiTicketByApi();
        if (ticketDto == null || !ticketDto.isSuccess()) {
            return null;
        }
        this.cacheProvider.setJsApiTicket(api.getAppId(), ticketDto);
        return ticketDto.getTicket();
    }

    public AccessTokenDto getCachedAccessToken(String appId) {
        return this.cacheProvider.getAccessToken(appId);
    }

    public JsApiTicketDto getCachedJsApiTicket(String appId) {
        return this.cacheProvider.getJsApiTicket(appId);
    }

    @Override
    public void close() {

    }
}
