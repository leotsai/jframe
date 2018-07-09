package org.jframe.infrastructure.mq;

import org.jframe.core.aliyun.mq.MqProducer;
import org.jframe.core.app.AppInitializer;
import org.jframe.core.extensions.KnownException;
import org.jframe.core.helpers.JsonHelper;
import org.jframe.core.logging.LogHelper;
import org.jframe.core.weixin.messaging.core.MessageBase;
import org.jframe.infrastructure.AppContext;
import org.jframe.infrastructure.configs.JframeMqTag;
import org.jframe.infrastructure.weixin.WeixinMessageDto;

/**
 * Created by leo on 2017-10-07.
 */
public class JframeMqProducer extends MqProducer implements AppInitializer {

    private final static JframeMqProducer instance = new JframeMqProducer();

    public static JframeMqProducer getInstance() {
        return instance;
    }

    private JframeMqProducer() {

    }

    public void tryPublish(JframeMqTag tag, String messaeBody) {
        try {
            super.publish(tag, messaeBody);
        } catch (Exception ex) {
            LogHelper.log("AliyunMqApi.publish", ex);
            throw new KnownException("写入数据队列失败了，请刷新页面重试");
        }
    }

    public void tryPublishWeixinMessage(MessageBase message) {
        WeixinMessageDto dto = new WeixinMessageDto(message);
        String msg = JsonHelper.serialize(dto);
        this.tryPublish(JframeMqTag.WEIXIN_MESSAGE, msg);
        LogHelper.log("WeixinMessage", msg);
    }

    @Override
    public String init() {
        super.initialize(AppContext.getMqProducerConfig());
        return this.getClass().getName() + " initialize success!";
    }
}
