package org.jframe.infrastructure.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.jframe.core.aliyun.mq.MqProducerConfig;

/**
 * Created by Leo on 2017/10/20.
 */
@Component
@PropertySource("/WEB-INF/app.properties")
public class MqProducerWpkConfig implements MqProducerConfig{

    @Value("${aliyun.mq.default.topic}")
    private String topic;

    @Value("${aliyun.mq.default.onsAddr}")
    private String onsAddr;

    @Value("${aliyun.mq.default.producerId}")
    private String producerId;

    @Value("${aliyun.mq.default.key}")
    private String key;

    @Value("${aliyun.mq.default.secret}")
    private String secret;


    //-------------------------------------------

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public String getSecret() {
        return this.secret;
    }

    @Override
    public String getTopic() {
        return this.topic;
    }

    @Override
    public String getProducerId() {
        return this.producerId;
    }

    @Override
    public String getONSAddr() {
        return this.onsAddr;
    }
}
