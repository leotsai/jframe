package org.jframe;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Leo on 2017/1/9.
 */
@Configuration
public class AppContext  implements ApplicationContextAware {
    public static final String ImageOriginalsRootFolder = "F:\\2016\\daben\\projects\\kids\\src\\_uploaded-image-originals\\";
    public static final String ImageSizedRootFolder = "F:\\2016\\daben\\projects\\kids\\src\\_uploaded-image-sizes\\";
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }


    public static <T> T getBean(String name) {
        return (T) context.getBean(name);
    }


    public static <T> T getBean(Class<T> clazz) {
        return (T) context.getBean(clazz);
    }

    public static boolean showFullError(){
        return true;
    }

    public static boolean printLogs(){
        return true;
    }


}
