package org.jframe.core.aliyun.mq;

import com.aliyun.openservices.ons.api.*;

import java.util.Properties;

/**
 * Created by leo on 2017-10-21.
 */
public abstract class MqConsumer {
    private String topic;
    private Consumer consumer;

    public void initialize(MqConsumerConfig config){
        if(this.consumer != null){
            System.out.println("Aliyun MQ Consumer duplicated initializing. topic name：" + this.topic);
            return;
        }
        this.topic = config.getTopic();
        try{
            this.consumer = ONSFactory.createConsumer(this.getProperties(config));
            this.consumer.subscribe(this.topic, "*", (message, context) -> this.consumeMessage(message, context));
            this.consumer.start();
            System.out.println("Aliyun MQ Consumer initialized. topic name：" + this.topic);
        }
        catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

    private Properties getProperties(MqConsumerConfig config){
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.AccessKey, config.getKey());
        properties.put(PropertyKeyConst.SecretKey, config.getSecret());
        properties.put(PropertyKeyConst.ConsumerId, config.getConsumerI());
        return properties;
    }

    protected abstract Action consumeMessage(Message message, ConsumeContext context);
}
