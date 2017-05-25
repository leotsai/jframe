package org.jframe.web.main.swagger;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import springfox.documentation.spring.web.json.Json;

/**
 * Created by leo on 2017-05-14.
 */
public class SwaggerFastJsonHttpMessageConverter extends FastJsonHttpMessageConverter {
    public SwaggerFastJsonHttpMessageConverter() {
        super();
        this.getFastJsonConfig().getSerializeConfig().put(Json.class, SwaggerJsonSerializer.instance);
    }
}
