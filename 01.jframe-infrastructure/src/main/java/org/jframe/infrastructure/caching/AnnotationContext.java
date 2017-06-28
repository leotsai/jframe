package org.jframe.infrastructure.caching;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by leo on 2017-05-26.
 */
public class AnnotationContext {

    private final static Map<Class, Map<Class, Annotation>> map = new HashMap<>();

    public static <T extends Annotation> T get(Class sourceClass, Class<T> annotationClass) {
        if (map.containsKey(sourceClass) == false) {
            Map<Class, Annotation> list = new HashMap<>();
            map.put(sourceClass, list);
        }
        Map<Class, Annotation> annotations = map.get(sourceClass);
        if (annotations.containsKey(annotationClass)) {
            return (T) annotations.get(annotationClass);
        }
        T value = (T) sourceClass.getAnnotation(annotationClass);
        annotations.put(annotationClass, value);
        return value;
    }

}
