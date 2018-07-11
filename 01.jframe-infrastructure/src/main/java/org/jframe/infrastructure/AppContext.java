package org.jframe.infrastructure;

import org.jframe.core.helpers.StringHelper;
import org.jframe.core.mongodb.MongodbConfig;
import org.jframe.core.redis.RedisConfig;
import org.jframe.core.web.Ioc;
import org.jframe.core.weixin.core.AppletConfig;
import org.jframe.core.weixin.core.WeixinPayConfig;
import org.jframe.infrastructure.configs.*;

import java.util.Map;

/**
 * Created by Leo on 2017/1/9.
 */

public class AppContext {

    private static String startupDirectory;

    public static <T> T getBean(Class<T> clazz) {
        return Ioc.get(clazz);
    }

    public static <T> Map<String, T> getBeans(Class<T> clazz) {
        return Ioc.getAll(clazz);
    }

    public static AppConfig getAppConfig() {
        return getBean(AppConfig.class);
    }

    public static AppletConfig getAppletConfig(Class<? extends AppletConfig> clazz) {
        return getBean(clazz);
    }

    public static JframeAlipayMobileConfig getAlipayMobileConfig() {
        return getBean(JframeAlipayMobileConfig.class);
    }

    public static MongodbConfig getMongodbConfig() {
        return getBean(MongodbConfig.class);
    }

    public static JframeMqConsumerConfig getMqConsumerConfig() {
        return getBean(JframeMqConsumerConfig.class);
    }

    public static JframeMqProducerConfig getMqProducerConfig() {
        return getBean(JframeMqProducerConfig.class);
    }

    public static OssImagesConfig getOssImagesConfig() {
        return getBean(OssImagesConfig.class);
    }

    public static OssInternalConfig getOssInternalConfig() {
        return getBean(OssInternalConfig.class);
    }

    public static OssPublicConfig getOssPublicConfig() {
        return getBean(OssPublicConfig.class);
    }

    public static JframeSmsConfig getSmsConfig() {
        return getBean(JframeSmsConfig.class);
    }

    public static RedisConfig getRedisConfig() {
        return getBean(RedisConfig.class);
    }

    public static JframeWeixinConfig getJframeWeixinConfig() {
        return getBean(JframeWeixinConfig.class);
    }

    public static JframeUnionpayConfig getUnionpayConfig() {
        return getBean(JframeUnionpayConfig.class);
    }

    public static HibernateConfig getHibernateConfig() {
        return getBean(HibernateConfig.class);
    }

    public static <T extends WeixinPayConfig> WeixinPayConfig getWeixinPayConfig(Class<T> clazz) {
        return getBean(clazz);
    }

    public static String getStartupDirectory() {
        return startupDirectory;
    }

    public static String setStartupDirectory(String value) {
        return startupDirectory = value;
    }

    public static String getImageUrl(String imageKey, ImageStyle style) {
        if (StringHelper.isNullOrWhitespace(imageKey)) {
            return "";
        }
        return getAppConfig().getCdnHostOfImages() + "/" + imageKey + "?x-oss-process=style/" + style.getStyle();
    }


    public static class RedisKeys {
        public static String weixinAccessToken(String appId) {
            return "wx_ak_" + appId;
        }

        public static String weixinJsApiTicket(String appId) {
            return "wx_jsticket_" + appId;
        }

        public static String captcha(String sessionId) {
            return "captcha_" + sessionId;
        }

        public static String smsPhone(String sessionId) {
            return "sms_" + sessionId;
        }

        public static String smsIpLimit(String ip) {
            return "sms_ip_" + ip;
        }

        public static String smsTimesLimit(String templateCodeAndPhone) {
            return "sms_times_" + templateCodeAndPhone;
        }

        public static String smsInterval(String templateCodeAndPhone) {
            return "sms_interval_" + templateCodeAndPhone;
        }

        public static String smsCaptcha(String templateCodeAndPhone) {
            return "sms_captcha_" + templateCodeAndPhone;
        }
    }
}
