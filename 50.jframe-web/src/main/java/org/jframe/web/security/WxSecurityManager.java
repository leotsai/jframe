package org.jframe.web.security;


import org.jframe.core.helpers.HttpHelper;
import org.jframe.core.helpers.RequestHelper;
import org.jframe.core.helpers.StringHelper;
import org.jframe.infrastructure.AppContext;
import org.jframe.infrastructure.helpers.CookieHelper;
import org.jframe.infrastructure.weixin.JframeWeixinApi;
import org.jframe.web.enums.WeixinAuthMode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author:qq
 * @date:2017/12/29
 */
public class WxSecurityManager {

    public static void redirectToWeixinAuth(HttpServletRequest request, HttpServletResponse response, String state) throws IOException {
        String returnUrl = AppContext.getAppConfig().getHost() + "/weixin/oauth?returnUrl=" + HttpHelper.urlEncode(HttpHelper.getPathAndQuery(request));
        String weixinLoginUrl = JframeWeixinApi.getInstance().getOAuthLoginUrl_UserInfo(returnUrl, state);
        response.sendRedirect(weixinLoginUrl);
    }

    public static void redirectToWeixinAutoLogin(HttpServletRequest request, HttpServletResponse response, String state) throws IOException {
        String returnUrl = AppContext.getAppConfig().getHost() + "/weixin/autoLogin?returnUrl=" + HttpHelper.urlEncode(HttpHelper.getPathAndQuery(request));
        String weixinLoginUrl = JframeWeixinApi.getInstance().getOAuthLoginUrl_UserInfo(returnUrl, state);
        response.sendRedirect(weixinLoginUrl);
    }

    public static void redirectToWeixinAuth_Silently(HttpServletRequest request, HttpServletResponse response, String state) throws IOException {
        String returnUrl = AppContext.getAppConfig().getHost() + "/weixin/silentOauth?returnUrl=" + HttpHelper.urlEncode(HttpHelper.getPathAndQuery(request));
        String silentOauthUrl = JframeWeixinApi.getInstance().getOAuthLoginUrl_Silently(returnUrl, state);
        response.sendRedirect(silentOauthUrl);
    }

    public static boolean isRedirectedToWeixinPayAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (RequestHelper.isInWeixin(request)) {
            String openId = CookieHelper.getWeixinOpenId();
            if (StringHelper.isNullOrWhitespace(openId)) {
                WxSecurityManager.redirectToWeixinAuth_Silently(request, response, WeixinAuthMode.OAUTH.name());
                return true;
            }
        }
        return false;
    }
}
