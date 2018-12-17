package org.jframe.core.web;


import org.jframe.core.app.AppInitializer;
import org.jframe.core.exception.KnownException;
import org.jframe.core.extensions.JDate;
import org.jframe.core.extensions.JList;
import org.jframe.core.extensions.PrintBuilder;
import org.jframe.core.logging.LogAppender;
import org.jframe.core.logging.LogHelper;
import org.jframe.core.logging.LoggingConfig;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

/**
 * Created by leo on 2017-06-28.
 */
public abstract class Application implements ApplicationListener {

    protected final PrintBuilder pb = new PrintBuilder();
    protected String startupDirectory;
    protected Properties appProperties;
    private final JList<AppInitializer> initializers = new JList<>();

    protected abstract void registerInitializers(JList<AppInitializer> initializers);

    protected abstract LogAppender getLogAppender();

    protected abstract LoggingConfig getLoggingConfig();

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextClosedEvent) {
            this.stop();
        } else if (event instanceof ContextStartedEvent || event instanceof ContextRefreshedEvent) {
            this.tryStart(event);
        }
    }

    private void tryStart(ApplicationEvent event) {
        try {
            this.pb.clear();
            StopWatch watch = new StopWatch();
            watch.start();


            this.pb.out("【" + JDate.now().toDateTimeString() + "】Application is starting now...");
            WebApplicationContext context = (WebApplicationContext) (((ApplicationContextEvent) event).getApplicationContext());
            Ioc.register(context);
            this.pb.out("Ioc is initialized. You can get all kinds of beans by Ioc.get(...).");
            this.startupDirectory = context.getServletContext().getRealPath("/");

            LogHelper.startLogger(this.getLoggingConfig(), this.getLogAppender());
            this.loadAppProperties();
            this.onBeforeInitializing();

            this.registerAllAreas();
            this.registerInitializers(this.initializers);
            this.runInitializers();
            this.onStarted();

            watch.stop();
            this.pb.out("Application started. Total time cost: " + watch.getLastTaskTimeMillis() + "ms" + LogHelper.getLineBreak());
            LogHelper.info().raw("app", this.pb.getMessages());
        } catch (Throwable ex) {
            LogHelper.fatal().log("app", ex);
        }
    }

    private void stop() {
        this.pb.clear();
        for (AppInitializer initializer : this.initializers) {
            try {
                initializer.close();
                this.pb.out(initializer.getClass().getName() + " closed successfully.");
            } catch (Exception ex) {
                if (ex instanceof KnownException) {
                    this.pb.err("AppInitializer.close.failed: " + ex.getMessage());
                } else {
                    this.pb.err("AppInitializer.close.failed. " + initializer.getClass().getName() + ": " + ex);
                }
            }
        }
        this.pb.out("Application stopped successfully.");
        LogHelper.info().raw("app", this.pb.getMessages());
        this.pb.clear();
        LogHelper.stopLogger();
    }

    private void runInitializers() {
        this.pb.out(LogHelper.getLineBreak() + "Start initializing " + this.initializers.size() + " initializers...");
        int failed = 0;
        StopWatch watch = new StopWatch();
        for (int i = 0; i < this.initializers.size(); i++) {
            AppInitializer appInitializer = this.initializers.get(i);
            watch.start();
            try {
                String result = appInitializer.init();
                watch.stop();
                this.pb.out((i + 1) + ": " + watch.getLastTaskTimeMillis() + " ms -> " + result);
            } catch (Throwable ex) {
                watch.stop();
                failed++;
                if (ex instanceof KnownException) {
                    this.pb.err((i + 1) + ": " + appInitializer.getClass().getSimpleName() + " error -> " + ex.getMessage());
                } else {
                    this.pb.err((i + 1) + ": " + appInitializer.getClass().getSimpleName() + " error -> " + ex);
                }
                this.pb.err("AppInitializer.initialize.failed. " + appInitializer.getClass().getName() + ":" + ex);
            }
        }
        this.pb.err("Total initializers：" + this.initializers.size() + ", success：" + (this.initializers.size() - failed) + ", failed：" + failed + LogHelper.getLineBreak());
    }

    protected void onBeforeInitializing() {

    }

    protected void onStarted() {

    }

    protected void loadAppProperties() {
        Path path = Paths.get(this.startupDirectory, "WEB-INF", "app.properties");
        try {
            this.appProperties = PropertiesLoaderUtils.loadProperties(new FileSystemResource(path.toFile()));
            this.pb.out("Load " + path + " success.");
        } catch (Exception ex) {
            this.pb.err("Load " + path + " error. " + LogHelper.getLineBreak() + ex);
        }
    }

    private void registerAllAreas() {
        Map<String, AreaRegistration> areas = Ioc.getAll(AreaRegistration.class);
        this.pb.out(LogHelper.getLineBreak() + "Registering " + areas.size() + " areas...");
        int i = 0;
        int failed = 0;
        for (String key : areas.keySet()) {
            i++;
            try {
                areas.get(key).run();
                this.pb.out(i + ": " + areas.get(key).getClass().getSimpleName() + " registered.");
            } catch (Exception ex) {
                this.pb.err(i + ": " + areas.get(key).getClass().getSimpleName() + " register failed.");
                failed++;
            }
        }
        this.pb.out("Register " + areas.size() + " areas. Success: " + (areas.size() - failed) + ". Failed: " + failed);
    }

}
