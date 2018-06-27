package org.jframe.web.security;

import org.jframe.core.helpers.HttpHelper;
import org.jframe.core.helpers.JsonHelper;
import org.jframe.data.entities.User;
import org.jframe.infrastructure.AppContext;
import org.jframe.infrastructure.helpers.CookieHelper;
import org.jframe.infrastructure.redis.JframeRedisSession;
import org.jframe.services.UserService;
import org.jframe.services.security.UserSession;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by leo on 2017-08-22.
 */
public class WebContext {
    private final static String CacheKey = "WebContext";

    public static WebContext getCurrent() {
        HttpServletRequest request = HttpHelper.getCurrentRequest();
        WebContext instance = (WebContext) request.getAttribute(CacheKey);
        if (instance == null) {
            instance = initializeFromRequest();
            request.setAttribute(CacheKey, instance);
        }
        return instance;
    }

    public static void refreshSession(Long userId) {
        User user = AppContext.getBean(UserService.class).get(userId);
        WebContext.refreshSession(user.getUsername());
    }

    public static void refreshSession(String username) {
        HttpServletRequest request = HttpHelper.getCurrentRequest();
        try (JframeRedisSession redisSession = new JframeRedisSession()) {
            String cacheKey = getCacheKey(username);
            if (redisSession.exists(cacheKey)) {
                redisSession.del(cacheKey);
                UserSession session = AppContext.getBean(UserService.class).getUserSession(username);
                session.setIp(request.getRemoteAddr());
                redisSession.setex(cacheKey, AppContext.getAppConfig().getSessionExpireSeconds(), JsonHelper.serialize(session));
            }
        }
    }

    private static WebContext initializeFromRequest() {
        WebIdentity identity = WebIdentity.parseFromRequest();
        if (identity == null) {
            return new WebContext(null);
        }
        try (JframeRedisSession redisSession = new JframeRedisSession()) {
            UserSession session;
            String cacheKey = getCacheKey(identity.getUsername());
            if (redisSession.exists(cacheKey)) {
                session = JsonHelper.deserialize(redisSession.get(cacheKey), UserSession.class);
                redisSession.expire(cacheKey, AppContext.getAppConfig().getSessionExpireSeconds());
                return new WebContext(session);
            }
            return new WebContext(null);
        }
    }

    private static String getCacheKey(String username) {
        return "SESSION-" + username;
    }


    private UserSession session;
    private boolean isAuthenticated;

    private WebContext(UserSession session) {
        this.session = session;
        this.isAuthenticated = session != null;
    }

    public boolean isAuthenticated() {
        return this.isAuthenticated;
    }

    public UserSession getSession() {
        return this.session;
    }

    public void login(WebIdentity identity) {
        HttpServletRequest request = HttpHelper.getCurrentRequest();
        UserService service = AppContext.getBean(UserService.class);
        this.session = service.getUserSession(identity.getUsername());
        this.session.setIp(request.getRemoteAddr());
        String cacheKey = getCacheKey(identity.getUsername());
        try (JframeRedisSession redisSession = new JframeRedisSession()) {
            redisSession.setex(cacheKey, AppContext.getAppConfig().getSessionExpireSeconds(), JsonHelper.serialize(this.session));
        }
        CookieHelper.setAuthToken(identity.toEncryptedRaw());
        CookieHelper.removeGuestUuid();
        this.isAuthenticated = true;
    }

    public void logout() {
        this.session = null;
        this.isAuthenticated = false;
        WebIdentity identity = WebIdentity.parseFromRequest();
        if (identity == null) {
            return;
        }
        try (JframeRedisSession redisSession = new JframeRedisSession()) {
            redisSession.del(getCacheKey(identity.getUsername()));
        }
        CookieHelper.removeCookiesOnLoggingOut();
    }


}
