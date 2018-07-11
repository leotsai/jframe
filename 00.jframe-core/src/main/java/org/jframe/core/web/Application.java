package org.jframe.core.web;

import org.jframe.core.app.AppInitializer;
import org.jframe.core.exception.KnownException;
import org.jframe.core.extensions.JList;
import org.jframe.core.logging.LogAppender;
import org.jframe.core.logging.LogHelper;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

/**
 * Created by leo on 2017-06-28.
 */
public abstract class Application implements ApplicationListener {

    protected String startupDirectory;
    protected Properties appProperties;
    private final JList<AppInitializer> initializers = new JList<>();

    protected abstract void registerInitializers(JList<AppInitializer> initializers);

    protected abstract LogAppender getLogAppender();

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextClosedEvent) {
            this.initializers.forEach(x -> {
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
        } else if (event instanceof ContextStartedEvent || event instanceof ContextRefreshedEvent) {
            WebApplicationContext context = (WebApplicationContext) (((ApplicationContextEvent) event).getApplicationContext());
            Ioc.register(context);
            this.startupDirectory = context.getServletContext().getRealPath("/");
            LogHelper.startLogger(this.getLogAppender());
            this.loadAppProperties();
            this.onBeforeInitializing();

            this.registerAllAreas();
            this.registerInitializers(this.initializers);
            int successCount = 0;
            int failCount = 0;
            for (int i = 0; i < this.initializers.size(); i++) {
                AppInitializer appInitializer = this.initializers.get(i);
                try {
                    Long startTime = System.currentTimeMillis();
                    String result = appInitializer.init();
                    Long finishTime = System.currentTimeMillis();
                    successCount++;
                    System.out.println((i + 1) + ":【" + (finishTime - startTime) + "ms】" + result);
                } catch (Throwable e) {
                    failCount++;
                    if (e instanceof KnownException) {
                        System.err.println("【failed】" + e.getMessage());
                        LogHelper.log("AppInitializer.initialize.failed", e.getMessage());
                    } else {
                        System.err.println("【failed】" + appInitializer.getClass().getName() + " initialize failed:" + e);
                        LogHelper.log("AppInitializer.initialize.failed", appInitializer.getClass().getName() + ":" + e);
                    }
                }
            }
            System.out.println("\nall initializer finished, total：" + this.initializers.size() + ", succeed：" + successCount + ", failed：" + failCount + "\n");
            this.onStarted();
        }
    }

    protected void onBeforeInitializing() {

    }

    protected void onStarted() {

    }

    private void loadAppProperties() {
        Path path = Paths.get(this.startupDirectory, "WEB-INF", "app.properties");
        try {
            this.appProperties = PropertiesLoaderUtils.loadProperties(new FileSystemResource(path.toFile()));
        } catch (Exception ex) {
            throw new KnownException("加载app.properties文件失败");
        }
    }

    private void registerAllAreas() {
        Map<String, AreaRegistration> areas = Ioc.getAll(AreaRegistration.class);
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
