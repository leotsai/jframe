package org.jframe.infrastructure.oss;

import org.jframe.core.aliyun.oss.OssBucket;
import org.jframe.core.app.AppInitializer;
import org.jframe.infrastructure.AppContext;

/**
 * Created by leo on 2017-08-18.
 */
public class JframeOssApi implements AppInitializer {
    private static final JframeOssApi instance = new JframeOssApi();

    private JframeOssApi() {

    }

    private static OssBucket images;
    private static OssBucket internal;
    private static OssBucket publik;

    public static JframeOssApi getInstance() {
        return instance;
    }

    public static OssBucket getImages() {
        return images;
    }

    public static OssBucket getPublic() {
        return publik;
    }

    public static OssBucket getInternal() {
        return internal;
    }

    private void initializeAll() {
        images = new OssBucket().initialize(AppContext.getOssImagesConfig());
        publik = new OssBucket().initialize(AppContext.getOssPublicConfig());
        internal = new OssBucket().initialize(AppContext.getOssInternalConfig());
    }

    private void closeAll() {
        images.close();
        publik.close();
        internal.close();
    }


    @Override
    public String init() {
        initializeAll();
        return this.getClass().getName() + " initialize success!";
    }

    @Override
    public void close() {
        closeAll();
    }
}
