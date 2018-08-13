package org.jframe.web.security;

import org.jframe.data.enums.VisualRole;

import java.lang.annotation.*;

/**
 * Created by Leo on 2017/1/8.
 */
@Documented
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreXssFilter {
    boolean value() default true;
}
