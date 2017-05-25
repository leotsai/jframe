package org.jframe;

import org.jframe.infrastructure.configs.AppConfig;
import org.jframe.infrastructure.data.HibernateSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Leo on 2017/1/9.
 */
@Configuration
public class AppContext  implements ApplicationContextAware {
    private static AppConfig appConfig;
    private static ApplicationContext context;
    public static final String SessionKey="JSESSIONID";

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
        appConfig = getBean(AppConfig.class);
        HibernateSessionFactory.getInstance().initialize();
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) context.getBean(name);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        return (T) context.getBean(clazz);
    }

    public static HttpServletRequest getCurrentRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }


    public static boolean showFullError(){
        return appConfig.isShowFullError();
    }

    public static boolean printLogs(){
        return appConfig.isPrintLogs();
    }

    public static boolean isTestServer(){
        return appConfig.isTestServer();
    }

    public static String getRootFolder() {
        return appConfig.getRootFolder();
    }

    public static String getHost() {
        return appConfig.getHost();
    }

    public static String getImageOriginalFolder() {
        return appConfig.getImageOriginalFolder();
    }

    public static String getImageSizesFolder() {
        return appConfig.getImageSizesFolder();
    }

    public static AppConfig getAppConfig(){
        return appConfig;
    }


}
