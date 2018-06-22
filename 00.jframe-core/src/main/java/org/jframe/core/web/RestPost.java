package org.jframe.core.web;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.*;

/**
 * Created by leo on 2017-08-30.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping(
        method = {RequestMethod.POST}
)
@ResponseBody
public @interface RestPost {
    @AliasFor(
            annotation = RequestMapping.class
    )
    String[] value() default {};
}
