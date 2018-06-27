package org.jframe.web.security;

import org.jframe.web.enums.WeixinAuthMode;

import java.lang.annotation.*;

/**
 * Created by leo on 2017-09-24.
 */
@Documented
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface WeixinAutoLogin {
    boolean ignored() default false;

    WeixinAuthMode mode() default WeixinAuthMode.LOGIN;
}
