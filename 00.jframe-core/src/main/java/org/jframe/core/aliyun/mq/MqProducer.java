package org.jframe.core.aliyun.mq;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;

import java.util.Properties;

/**
 * Created by leo on 2017-10-07.
 */
public abstract class MqProducer {

    private String topic;
    private Producer producer;

    public void initialize(MqProducerConfig config){
        if(this.producer != null){
            return;
        }
        this.topic = config.getTopic();
        try{
            this.producer = ONSFactory.createProducer(this.getProperties(config));
            this.producer.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

    public void close(){
        this.producer.shutdown();
    }

    private Properties getProperties(MqProducerConfig config){
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.AccessKey, config.getKey());
        properties.put(PropertyKeyConst.SecretKey, config.getSecret());
        properties.put(PropertyKeyConst.ProducerId, config.getProducerId());
        properties.put(PropertyKeyConst.ONSAddr, config.getONSAddr());
        return properties;
    }

    public void shutdown(){
        this.producer.shutdown();
    }

    public void publish(MqTag tag, String messaeBody){
        Message message = new Message(this.topic, tag.getKey(), messaeBody.getBytes());
        producer.send(message);
    }


}
