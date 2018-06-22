package org.jframe.infrastructure.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.jframe.core.aliyun.oss.OssConfig;

/**
 * Created by Leo on 2017/10/20.
 */
@Component
@PropertySource("/WEB-INF/app.properties")
public class OssPublicConfig implements OssConfig{

    @Value("${aliyun.oss.public.key}")
    private String key;

    @Value("${aliyun.oss.public.secret}")
    private String secret;

    @Value("${aliyun.oss.public.bucketName}")
    private String bucketName;

    @Value("${aliyun.oss.public.endPoint}")
    private String endPoint;

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public String getSecret() {
        return this.secret;
    }

    @Override
    public String getBucketName() {
        return this.bucketName;
    }

    @Override
    public String getEndPoint() {
        return this.endPoint;
    }
}
