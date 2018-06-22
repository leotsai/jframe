package org.jframe.core.weixin;

import org.jframe.core.extensions.KnownException;
import org.jframe.core.helpers.JsonHelper;
import org.jframe.core.http.WebClient;
import org.jframe.core.weixin.core.AppletConfig;
import org.jframe.core.weixin.core.WxKeyManager;
import org.jframe.core.weixin.core.WxUrls;
import org.jframe.core.weixin.core.dtos.AccessTokenDto;
import org.jframe.core.weixin.core.dtos.AppletOpenIdDto;
import org.jframe.core.weixin.core.dtos.GetQrCodeDto;
import org.jframe.core.weixin.core.helpers.WxHttpHelper;
import org.jframe.core.weixin.core.parameters.AppletQrCodeParameter;

import java.io.InputStream;

/**
 * @author xiaojin
 * @desc 微信小程序通用接口
 * @date 2018-04-20 11:01
 */
public class AppletApi {

    private AppletConfig config;

    public AppletApi() {
    }

    public AppletApi(AppletConfig config) {
        this.config = config;
    }

    public AppletConfig getConfig() {
        return config;
    }

    private String getCachedAccessToken() {
        return WxKeyManager.getInstance().getAccessToken(this);
    }

    public String getAppId() {
        return this.config == null ? null : this.config.getAppId();
    }

    public AccessTokenDto getAccessTokenByApi() {
        return WxHttpHelper.getApiDto(AccessTokenDto.class, WxUrls.getAccessToken(this.config.getAppId(), this.config.getAppSecret()));
    }

    public InputStream getAppletQrCodeStream(String page, String scene) throws Exception {
        String data = JsonHelper.serialize(new AppletQrCodeParameter(page, scene));
        WebClient webClient = new WebClient();
        InputStream inputStream = webClient.uploadStream(WxUrls.generateAppletQrCode(this.getCachedAccessToken()), data);
        return inputStream;
    }

    public AppletOpenIdDto getOpenIdByCode(String code) {
        return WxHttpHelper.getApiDto(AppletOpenIdDto.class, WxUrls.getOpenIdForApplet(code, this.config.getAppId(), this.config.getAppSecret()));
    }

}
