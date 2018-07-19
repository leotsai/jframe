package org.jframe.infrastructure.helpers;

import org.jframe.core.extensions.Action;
import org.jframe.core.extensions.JList;
import org.jframe.core.helpers.HttpHelper;
import org.jframe.core.helpers.StringHelper;
import org.jframe.infrastructure.AppContext;

import javax.servlet.http.Cookie;
import java.util.Objects;

/**
 * Created by Leo on 2017/9/25.
 */
public class CookieHelper {

    public static String getAuthToken() {
        return getValue(AppContext.getAppConfig().getAuthCookieName());
    }

    public static void setAuthToken(String token) {
        setValue(AppContext.getAppConfig().getAuthCookieName(), token);
    }

    public static void removeAuthToken() {
        remove(AppContext.getAppConfig().getAuthCookieName());
    }

    public static String getGuestUuid() {
        return getValue("GUESTED");
    }

    public static void setGuestUuid(String guestUuid) {
        setValue("GUESTED", guestUuid, 3600 * 24 * 365);
    }

    public static void removeGuestUuid() {
        remove("GUESTED");
    }

    public static String getWeixinOpenId() {
        return getValue("WXOPENID");
    }

    public static void setWeixinOpenId(String value) {
        setValue("WXOPENID", value, 3600 * 24 * 365);
    }

    public static String getFromUserId() {
        return getValue("FROMUSER");
    }

    public static void setFromUserId(String value) {
        setValue("FROMUSER", value, 3600 * 24 * 365);
    }

    public static void removeWeixinOpenId() {
        remove("WXOPENID");
    }

    public static void entryCookie(String name, int expireSeconds, Action action) {
        String value = getValue(name);
        if (!StringHelper.isNullOrWhitespace(value)) {
            return;
        }
        action.apply();
        setValue(name, "1", expireSeconds);
    }

    public static void removeCookiesOnLoggingOut() {
        CookieHelper.removeAuthToken();
        CookieHelper.removeWeixinOpenId();
    }

    private static String getValue(String name) {
        Cookie cookie = JList.from(HttpHelper.getCurrentRequest().getCookies()).firstOrNull(x -> Objects.equals(x.getName(), name));
        return cookie == null ? null : cookie.getValue();
    }

    private static void setValue(String name, String value, int expireSeconds) {
        Cookie cookie = new Cookie(name, value);
//        cookie.setDomain(AppContext.getAppConfig().getCookieDomain());
        cookie.setMaxAge(expireSeconds);
        cookie.setPath("/");
        HttpHelper.getCurrentResponse().addCookie(cookie);
    }

    private static void setValue(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        HttpHelper.getCurrentResponse().addCookie(cookie);
    }

    private static void remove(String name) {
        Cookie cookie = new Cookie(name, null);
//        cookie.setDomain(AppContext.getAppConfig().getCookieDomain());
        cookie.setMaxAge(0);
        cookie.setPath("/");
        HttpHelper.getCurrentResponse().addCookie(cookie);
    }


}
