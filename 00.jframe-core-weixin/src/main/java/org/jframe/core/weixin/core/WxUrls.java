package org.jframe.core.weixin.core;

import org.jframe.core.helpers.HttpHelper;
import org.jframe.core.weixin.core.enums.MediaType;

/**
 * Created by leo on 2017-09-23.
 */
public class WxUrls {

    public static final String CREATE_PAY_REQUEST = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    public static String getAccessToken(String appId, String appSecret) {
        return "https://api.weixin.qq.com/cgi-bin/token"
                + String.format("?grant_type=client_credential&appid=%s&secret=%s", appId, appSecret);
    }

    public static String getJsApiTicket(String accessToken) {
        return "https://api.weixin.qq.com/cgi-bin/ticket/getticket"
                + String.format("?access_token=%s&type=jsapi", accessToken);
    }

    public static String getUserInfo(String openId, String accessToken) {
        return "https://api.weixin.qq.com/cgi-bin/user/info"
                + String.format("?access_token=%s&openid=%s&lang=zh_CN", accessToken, openId);
    }

    public static String getUserInfo_OAuthCallback(String openId, String oauthAccessToken) {
        return "https://api.weixin.qq.com/sns/userinfo"
                + String.format("?access_token=%s&openid=%s&lang=zh_CN", oauthAccessToken, openId);
    }

    public static String getOpenIdByCode(String code, String appId, String appSecret) {
        return "https://api.weixin.qq.com/sns/oauth2/access_token"
                + String.format("?appid=%s&secret=%s&code=%s&grant_type=authorization_code", appId, appSecret, code);
    }

    public static String getMenu(String accessToken) {
        return "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=" + accessToken;
    }

    public static String saveMenu(String accessToken) {
        return "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + accessToken;
    }

    public static String deleteMenu(String accessToken) {
        return "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=" + accessToken;
    }

    public static String getMediaDownloadUrl(String mediaId, String accessToken) {
        return "http://file.api.weixin.qq.com/cgi-bin/media/get"
                + String.format("?access_token=%s&media_id=%s", accessToken, mediaId);
    }

    public static String sendMessage(boolean isTemplate, String accessToken) {
        return "https://api.weixin.qq.com/cgi-bin/message/" + (isTemplate ? "template" : "custom") + "/send?access_token=" + accessToken;
    }

    public static String getOAuthLoginUrl_Silently(String returnUrl, String appId, String state) {
        return "https://open.weixin.qq.com/connect/oauth2/authorize"
                + String.format("?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=%s#wechat_redirect", appId, HttpHelper.urlEncode(returnUrl), state);
    }

    public static String getOAuthLoginUrl_UserInfo(String returnUrl, String appId, String state) {
        return "https://open.weixin.qq.com/connect/oauth2/authorize"
                + String.format("?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=%s#wechat_redirect", appId, HttpHelper.urlEncode(returnUrl), state);
    }

    public static String getResource(String accessToken) {
        return "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=" + accessToken;
    }

    public static String generateQrCode(String accessToken) {
        return "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + accessToken;
    }

    public static String generateAppletQrCode(String accessToken) {
        return "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;
    }

    public static String getOpenIdForApplet(String code, String appId, String secret) {
        return String.format("https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code", appId, secret, code);
    }

    public static String showQrCode(String ticket) {
        return "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + HttpHelper.urlEncode(ticket);
    }

    public static String uploadMedia(MediaType type, String accessToken) {
        return "https://api.weixin.qq.com/cgi-bin/media/upload"
                + String.format("?access_token=%s&type=%s", accessToken, type);
    }

    public static String shortenUrl(String accessToken) {
        return "https://api.weixin.qq.com/cgi-bin/shorturl?access_token=" + accessToken;
    }

    public static String sendMassMessage(String accessToken) {
        return "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=" + accessToken;
    }

    public static String getUserTags(String accessToken) {
        return "https://api.weixin.qq.com/cgi-bin/tags/get?access_token=" + accessToken;
    }

    public static String getComments(String accessToken) {
        return "https://api.weixin.qq.com/cgi-bin/comment/list?access_token=" + accessToken;
    }
}
