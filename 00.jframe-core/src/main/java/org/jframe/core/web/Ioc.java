package org.jframe.core.web;

import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * Created by Leo on 2018/7/3.
 */
public class Ioc {

    private static ApplicationContext context;

    static void register(ApplicationContext context){
        Ioc.context = context;
    }

    public static <T> T get(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static <T> Map<String, T> getAll(Class<T> clazz) {
        return context.getBeansOfType(clazz);
    }


}
