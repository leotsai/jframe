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

    protected String startupDirectory;
    protected Properties appProperties;
    private final JList<AppInitializer> initializers = new JList<>();

    protected abstract void registerInitializers(JList<AppInitializer> initializers);

    protected abstract LogAppender getLogAppender();

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextClosedEvent) {
            this.stop();
        } else if (event instanceof ContextStartedEvent || event instanceof ContextRefreshedEvent) {
            this.tryStart(event);
        }
    }

    private void tryStart(ApplicationEvent event) {
        try{
            StopWatch watch = new StopWatch();
            watch.start();

            System.out.println("\nApplication is starting now...");
            WebApplicationContext context = (WebApplicationContext) (((ApplicationContextEvent) event).getApplicationContext());
            Ioc.register(context);
            System.out.println("Ioc is initialized. You can get all kinds of beans by Ioc.get(...).");
            this.startupDirectory = context.getServletContext().getRealPath("/");
            LogHelper.startLogger(this.getLogAppender());
            this.loadAppProperties();
            this.onBeforeInitializing();

            this.registerAllAreas();
            this.registerInitializers(this.initializers);
            this.runInitializers();
            this.onStarted();
            watch.stop();
            System.out.println("\nApplication started. Total time cost: " + watch.getLastTaskTimeMillis() + "ms\n");
        }
        catch (Throwable ex){
            ex.printStackTrace();
        }
    }

    private void stop() {
        this.initializers.forEach(x -> {
            try {
                x.close();
                System.out.println(x.getClass().getName() + " closed");
            } catch (Exception ex) {
                if (ex instanceof KnownException) {
                    LogHelper.log("AppInitializer.close.failed", ex.getMessage());
                } else {
                    LogHelper.log("AppInitializer.close.failed", x.getClass().getName() + ":" + ex);
                }
            }
        });
        LogHelper.stopLogger();
    }

    private void runInitializers() {
        System.out.println("\nStart initializing " + this.initializers.size() + " initializers...");
        int failed = 0;
        StopWatch watch = new StopWatch();
        for (int i = 0; i < this.initializers.size(); i++) {
            AppInitializer appInitializer = this.initializers.get(i);
            watch.start();
            try {
                String result = appInitializer.init();
                watch.stop();
                System.out.println((i + 1) + ": " + watch.getLastTaskTimeMillis() + " ms -> " + result);
            } catch (Throwable ex) {
                watch.stop();
                failed++;
                if (ex instanceof KnownException) {
                    System.err.println((i + 1) + ": " + appInitializer.getClass().getSimpleName() + " error -> " + ex.getMessage());
                } else {
                    System.err.println((i + 1) + ": " + appInitializer.getClass().getSimpleName() + " error -> " + ex);
                }
                LogHelper.log("AppInitializer.initialize.failed", appInitializer.getClass().getName() + ":" + ex);
            }
        }
        System.out.println("Total initializers：" + this.initializers.size() + ", success：" + (this.initializers.size() - failed) + ", failed：" + failed + "\n");
    }

    protected void onBeforeInitializing() {

    }

    protected void onStarted() {

    }

    private void loadAppProperties() {
        Path path = Paths.get(this.startupDirectory, "WEB-INF", "app.properties");
        try {
            this.appProperties = PropertiesLoaderUtils.loadProperties(new FileSystemResource(path.toFile()));
            System.out.println("Load " + path + " success.");
        } catch (Exception ex) {
            System.err.println("Load " + path + " error. \n" + ex);
        }
    }

    private void registerAllAreas() {
        Map<String, AreaRegistration> areas = Ioc.getAll(AreaRegistration.class);
        System.out.println("\nRegistering " + areas.size() + " areas...");
        int i = 0;
        int failed = 0;
        for (String key : areas.keySet()) {
            i++;
            try {
                areas.get(key).run();
                System.out.println(i + ": " + areas.get(key).getClass().getSimpleName() + " registered.");
            } catch (Exception ex) {
                LogHelper.log("系统-AreaRegistration", ex);
                System.err.println(i + ": " + areas.get(key).getClass().getSimpleName() + " register failed.");
                failed++;
            }
        }
        System.out.println("Register " + areas.size() + " areas. Success: " + (areas.size() - failed) + ". Failed: " + failed);
    }

}
