package org.jframe.web;

import org.jframe.core.app.AppInitializer;
import org.jframe.core.extensions.JList;
import org.jframe.core.extensions.KnownException;
import org.jframe.core.helpers.ClassHelper;
import org.jframe.core.hibernate.DtoResultTransformer;
import org.jframe.core.logging.LogHelper;
import org.jframe.core.unionpay.sdk.SDKConfig;
import org.jframe.core.weixin.core.WxKeyManager;
import org.jframe.data.JframeHibernateSessionFactory;
import org.jframe.data.caching.DbCacheContext;
import org.jframe.infrastructure.AppContext;
import org.jframe.infrastructure.alipay.JframeAlipayApi;
import org.jframe.infrastructure.logging.JframeMongoLogAppender;
import org.jframe.infrastructure.mongodb.JframeMongoDatabaseFactory;
import org.jframe.infrastructure.redis.JframeRedisPoolContext;
import org.jframe.infrastructure.redis.RedisWxCacheProvider;
import org.jframe.infrastructure.security.JframeCrypto;
import org.jframe.infrastructure.sms.JframeSmsApi;
import org.jframe.infrastructure.unionpay.JframeUnionpayApi;
import org.jframe.infrastructure.weixin.JframeWeixinPayApi;
import org.jframe.web.core.AreaRegistration;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

/**
 * Created by leo on 2017-06-28.
 */
public class MvcApplicationListner implements ApplicationListener {

    private final static JList<AppInitializer> INITIALIZERS = new JList<>();


    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {

        if (applicationEvent instanceof ContextClosedEvent) {
            INITIALIZERS.forEach(x -> {
                try {
                    x.close();
                    System.out.println(x.getClass().getName() + " closed");
                } catch (Exception e) {
                    if (e instanceof KnownException) {
                        LogHelper.log("AppInitializer.close.failed", e.getMessage());
                    } else {
                        LogHelper.log("AppInitializer.close.failed", x.getClass().getName() + ":" + e);
                    }
                }
            });
            LogHelper.stopLogger();
        } else if (applicationEvent instanceof ContextStartedEvent || applicationEvent instanceof ContextRefreshedEvent) {
            Properties appProperties = getAppProperties();

            LogHelper.startLogger(new JframeMongoLogAppender(AppContext.getAppConfig().getLogsDbName()), true);
            DtoResultTransformer.checkDtoEntityConstructors(ClassHelper.getClasses(AppContext.getStartupDirectory(), AppContext.getAppConfig().getEntityPackage() + "."));
            SDKConfig.getConfig().loadProperties(appProperties);
            this.registerAllAreas();

            INITIALIZERS.add(JframeMongoDatabaseFactory.getInstance());
            INITIALIZERS.add(JframeHibernateSessionFactory.getInstance().setConfiguration(AppContext.getStartupDirectory()));
            INITIALIZERS.add(JframeCrypto.getInstance());
            INITIALIZERS.add(WxKeyManager.getInstance().setCacheProvider(new RedisWxCacheProvider()));
            INITIALIZERS.add(JframeRedisPoolContext.getInstance());
            INITIALIZERS.add(JframeSmsApi.getInstance());
            INITIALIZERS.add(JframeUnionpayApi.getInstance());
            INITIALIZERS.add(JframeAlipayApi.getInstance());
            INITIALIZERS.add(JframeWeixinPayApi.getInstance());
            INITIALIZERS.add(DbCacheContext.getInstance());

            INITIALIZERS.forEach(x -> {
                try {
                    x.initialize();
                    System.out.println(x.getClass().getName() + " initialized");
                } catch (Exception e) {
                    if (e instanceof KnownException) {
                        LogHelper.log("AppInitializer.initialize.failed", e.getMessage());
                    } else {
                        LogHelper.log("AppInitializer.initialize.failed", x.getClass().getName() + ":" + e);
                    }
                }
            });
        }
    }

    private Properties getAppProperties() {
        Path path = Paths.get(AppContext.getStartupDirectory(), "WEB-INF", "app.properties");
        try {
            return PropertiesLoaderUtils.loadProperties(new FileSystemResource(path.toFile()));
        } catch (Exception ex) {
            throw new KnownException("加载app.properties文件失败");
        }
    }

    private void registerAllAreas() {
        Map<String, AreaRegistration> areas = AppContext.getBeans(AreaRegistration.class);
        for (String key : areas.keySet()) {
            try {
                areas.get(key).run();
            } catch (Exception ex) {
                LogHelper.log("系统-AreaRegistration", ex);
                throw new KnownException("\n\nAreaRegistration 出现严重错误\n\n");
            }
        }
    }
}
