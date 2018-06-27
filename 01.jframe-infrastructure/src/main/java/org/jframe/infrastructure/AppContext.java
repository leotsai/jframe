package org.jframe.infrastructure;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;
import org.jframe.infrastructure.configs.*;
import org.jframe.core.helpers.StringHelper;
import org.jframe.core.mongodb.MongodbConfig;
import org.jframe.core.redis.RedisConfig;
import org.jframe.core.weixin.core.AppletConfig;
import org.jframe.core.weixin.core.WeixinPayConfig;

import java.util.Map;

/**
 * Created by Leo on 2017/1/9.
 */
@Configuration
public class AppContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    private static String startupDirectory;
    private static boolean isRefundApiDisabled = false;

    /**
     * 版本号
     */
    public static String PC_VERSION = "V1.1.8";
    public static String WEIXIN_VERSION = "V1.1.9";
    public static final String VERSION_DATE = "2018-02-06";

    public static final String SHARE_COOKIE_NAME = "FROMUSER";
    public static final int SHARE_COOKIE_EXPIRE_MINUTES = 60 * 24 * 365;//one year
    public static final String SHARE_URL = "/site/page/invite-friend";

    public static final String HTMLPAGE_URL = "/site/page/";
    public static final String ARTICLE_URL = "/pa/";

    public static boolean isIsRefundApiDisabled() {
        return isRefundApiDisabled;
    }

    public static void setIsRefundApiDisabled(boolean isRefundApiDisabled) {
        AppContext.isRefundApiDisabled = isRefundApiDisabled;
    }

    public static class RedisKeys {
        public static final String BANNER = "redis_banner";
        public static final String FEATURE_NEW = "redis_feature_new";
        public static final String FEATURE_QUALITY = "redis_feature_quality";
        public static final String PACKAGE = "redis_cached_package";
        public static final String QUALITY_PACKAGE_DETAIL = "redis_cached_quality_package_detail";
        public static final String TAG = "redis_tag";

        public static String captcha(String sessionId) {
            return "captcha_" + sessionId;
        }

        public static String smsPhone(String sessionId) {
            return "sms_phone_" + sessionId;
        }

        public static String weixinAccessToken(String appId) {
            return "wx_access_token" + appId;
        }

        public static String weixinJsApiTicket(String appId) {
            return "wx_jsapi_ticket" + appId;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        AppContext.applicationContext = applicationContext;
        AppContext.startupDirectory = ((WebApplicationContext) applicationContext).getServletContext().getRealPath("/");

        this.setSystemOut();
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static <T> Map<String, T> getBeans(Class<T> clazz) {
        return applicationContext.getBeansOfType(clazz);
    }

    private void setSystemOut() {
//        if (!appConfig.isTestServer()) {
//            System.setOut(new PrintStream(new NothingOutputStream()));
//        }
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

    public static JframeMqProducerConfig getMqConfig() {
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

    public static <T extends WeixinPayConfig> WeixinPayConfig getWeixinPayConfig(Class<T> clazz) {
        return getBean(clazz);
    }

    public static String getStartupDirectory() {
        return startupDirectory;
    }

    public static String getImageUrl(String imageKey, ImageStyle style) {
        if (StringHelper.isNullOrWhitespace(imageKey)) {
            return "";
        }
        return getAppConfig().getCdnHostOfImages() + "/" + imageKey + "?x-oss-process=style/" + style.getStyle();
    }


}
