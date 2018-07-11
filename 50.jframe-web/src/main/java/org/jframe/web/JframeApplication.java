package org.jframe.web;

import org.jframe.core.app.AppInitializer;
import org.jframe.core.extensions.JList;
import org.jframe.core.logging.LogAppender;
import org.jframe.core.web.Application;
import org.jframe.core.weixin.core.WxKeyManager;
import org.jframe.data.JframeHibernateSessionFactory;
import org.jframe.data.caching.DbCacheContext;
import org.jframe.infrastructure.AppContext;
import org.jframe.infrastructure.alipay.JframeAlipayApi;
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
    }

    @Override
    protected LogAppender getLogAppender() {
        JframeMongoDatabaseFactory.getInstance().initialize();
        return new JframeMongoLogAppender(AppContext.getAppConfig().getLogsDbName(), true);
    }


    @Override
    protected void onBeforeInitializing() {
        AppContext.setStartupDirectory(super.startupDirectory);
    }


}
