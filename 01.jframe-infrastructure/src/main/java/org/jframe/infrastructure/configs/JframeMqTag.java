package org.jframe.infrastructure.configs;

import org.jframe.core.aliyun.mq.MqTag;
import org.jframe.core.extensions.JList;
import org.jframe.infrastructure.weixin.WeixinMessageDto;

import java.util.Objects;

/**
 * Created by leo on 2017-10-07.
 * 重要：新增tag时，只能往已有枚举值后面追加，禁止向中间插入新值
 */
public enum JframeMqTag implements MqTag {
    TEST("0", "测试专用", null),
    WEIXIN_MESSAGE("1", "微信消息", WeixinMessageDto.class),;

    private final String key;
    private final String text;
    private final Class clazz;

    JframeMqTag(String key, String text, Class clazz) {
        this.key = key;
        this.text = text;
        this.clazz = clazz;
    }

    public static JframeMqTag fromKey(String key) {
        JframeMqTag mqTag = JList.from(JframeMqTag.values()).firstOrNull(x -> Objects.equals(x.getKey(), key));
        return mqTag == null ? JframeMqTag.TEST : mqTag;
    }

    public String getText() {
        return text;
    }

    @Override
    public String getKey() {
        return key;
    }

    public Class getClazz() {
        return clazz;
    }
}
