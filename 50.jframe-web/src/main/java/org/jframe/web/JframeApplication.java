package org.jframe.web;

import org.jframe.core.app.AppInitializer;
import org.jframe.core.extensions.JList;
import org.jframe.core.logging.LogAppender;
import org.jframe.core.logging.LoggingConfig;
import org.jframe.core.web.Application;
import org.jframe.core.weixin.core.WxKeyManager;
import org.jframe.data.JframeHibernateSessionFactory;
import org.jframe.data.caching.DbCacheContext;
import org.jframe.infrastructure.AppContext;
import org.jframe.infrastructure.alipay.JframeAlipayApi;
import org.jframe.infrastructure.cache.CacheManager;
import org.jframe.infrastructure.cache.RedisCache;
import org.jframe.infrastructure.mongodb.JframeMongoDatabaseFactory;
import org.jframe.infrastructure.mq.JframeMqConsumer;
import org.jframe.infrastructure.mq.JframeMqProducer;
import org.jframe.infrastructure.oss.JframeOssApi;
import org.jframe.infrastructure.redis.JframeRedisPoolContext;
import org.jframe.infrastructure.redis.RedisWxCacheProvider;
import org.jframe.infrastructure.security.JframeCrypto;
import org.jframe.infrastructure.sms.JframeSmsApi;
import org.jframe.infrastructure.unionpay.JframeUnionpayApi;
import org.jframe.infrastructure.weixin.JframeWeixinPayApi;
import org.jframe.services.logging.JframeMongoLogAppender;
import org.jframe.web.security.PermissionRegistery;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by leo on 2017-06-28.
 */
public class JframeApplication extends Application {

    @Override
    protected void registerInitializers(JList<AppInitializer> initializers) {
        initializers.add(JframeHibernateSessionFactory.getInstance());
        initializers.add(JframeCrypto.getInstance());
        initializers.add(JframeRedisPoolContext.getInstance());
        initializers.add(WxKeyManager.getInstance().setCacheProvider(new RedisWxCacheProvider()));
        initializers.add(JframeSmsApi.getInstance());
        initializers.add(JframeOssApi.getInstance());
        initializers.add(JframeMqProducer.getInstance());
        initializers.add(JframeMqConsumer.getInstance());
        initializers.add(JframeUnionpayApi.getInstance().setProperties(super.appProperties));
        initializers.add(JframeAlipayApi.getInstance());
        initializers.add(JframeWeixinPayApi.getInstance());
        initializers.add(PermissionRegistery.getInstance());
        initializers.add(DbCacheContext.getInstance());
        initializers.add(CacheManager.getInstance().setCache(new RedisCache()));
    }

    @Override
    protected LogAppender getLogAppender() {
        System.out.println(JframeMongoDatabaseFactory.getInstance().init());
        return new JframeMongoLogAppender(AppContext.getAppConfig().getLogsDbName());
    }

    @Override
    protected LoggingConfig getLoggingConfig() {
        return new LoggingConfig() {
            @Override
            public boolean printStackTrace() {
                return true;
            }

            @Override
            public String getServerName() {
                return "dev";
            }

            @Override
            public boolean appendHttpHeaders() {
                return true;
            }

            @Override
            public int getFlushIntervalSeconds() {
                return 10;
            }

            @Override
            public int getInitialDelaySeconds() {
                return 10;
            }
        };
    }


    @Override
    protected void onBeforeInitializing() {
        AppContext.setStartupDirectory(super.startupDirectory);
        System.out.println("AppContext.startupDirectory is already set to: " + super.startupDirectory);
    }


}
