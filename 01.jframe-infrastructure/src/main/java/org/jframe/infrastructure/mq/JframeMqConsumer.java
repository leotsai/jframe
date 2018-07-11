package org.jframe.infrastructure.mq;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import org.jframe.core.aliyun.mq.MqConsumer;
import org.jframe.core.app.AppInitializer;
import org.jframe.core.exception.KnownException;
import org.jframe.core.helpers.JsonHelper;
import org.jframe.core.logging.LogHelper;
import org.jframe.infrastructure.AppContext;
import org.jframe.infrastructure.configs.JframeMqTag;
import org.jframe.infrastructure.weixin.JframeWeixinApi;
import org.jframe.infrastructure.weixin.WeixinMessageDto;

import java.io.UnsupportedEncodingException;

/**
 * Created by leo on 2017-10-07.
 */
public class JframeMqConsumer extends MqConsumer implements AppInitializer {

    private final static JframeMqConsumer instance = new JframeMqConsumer();

    public static JframeMqConsumer getInstance() {
        return instance;
    }

    private JframeMqConsumer() {

    }


    @Override
    protected Action consumeMessage(Message message, ConsumeContext context) {
        if (AppContext.getAppConfig().isTestServer()) {
            try {
                System.out.println("tag:" + message.getTag() + ", body: " + new String(message.getBody(), "utf-8"));
            } catch (Exception ex) {

            }
        }

        try {
            JframeMqTag tag = JframeMqTag.fromKey(message.getTag());
            switch (tag) {
                case TEST:
                    break;
                case WEIXIN_MESSAGE:
                    WeixinMessageDto messageDto = deserialize(WeixinMessageDto.class, message.getBody());
                    JframeWeixinApi.getInstance().trySendMessage(messageDto.getJson(), messageDto.isTemplate());
                    break;
                default:
            }
            return Action.CommitMessage;
        } catch (Throwable e) {
            LogHelper.log("AliMqConsumer.consumeMessage", e);
            throw new KnownException("处理阿里云队列异常！");
        }
    }

    private <T> T deserialize(Class<T> clazz, byte[] body) throws UnsupportedEncodingException {
        return JsonHelper.deserialize(this.getMessageString(body), clazz);
    }

    private String getMessageString(byte[] body) throws UnsupportedEncodingException {
        return new String(body, "utf-8");
    }

    @Override
    public String init() {
        super.initialize(AppContext.getMqConsumerConfig());
        return this.getClass().getName() + " initialize success!";
    }

    @Override
    public void close() {
    }
}
