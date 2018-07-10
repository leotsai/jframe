package org.jframe.services;

import org.jframe.core.helpers.RequestHelper;
import org.jframe.infrastructure.AppContext;
import org.jframe.infrastructure.redis.JframeRedisSession;

/**
 * redis 公共接口
 *
 * @author xiaojin
 * @date 2017-09-01 09:59
 */
public class RedisApi {

    /**
     * 设置当前session的图片验证码
     *
     * @param code
     */
    public static void setCurrentCaptcha(String code) {
        String key = AppContext.RedisKeys.captcha(RequestHelper.getSessionId());
        int expireSeconds = 600;
        try (JframeRedisSession session = new JframeRedisSession()) {
            session.setex(key, expireSeconds, code);
        }
    }

    /**
     * 获取当前session的图片验证码
     *
     * @return
     */
    public static String getCurrentCaptcha() {
        String key = AppContext.RedisKeys.captcha(RequestHelper.getSessionId());
        try (JframeRedisSession session = new JframeRedisSession()) {
            String code = session.get(key);
            session.del(key);
            return code;
        }
    }

    public static void setCurrentSmsPhone(String phone) {
        String key = AppContext.RedisKeys.smsPhone(RequestHelper.getSessionId());
        int expireSeconds = 600;
        try (JframeRedisSession session = new JframeRedisSession()) {
            session.setex(key, expireSeconds, phone);
        }
    }

    public static String getCurrentSmsPhone() {
        String key = AppContext.RedisKeys.smsPhone(RequestHelper.getSessionId());
        try (JframeRedisSession session = new JframeRedisSession()) {
            String phone = session.get(key);
            return phone;
        }
    }

}
