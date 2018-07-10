package org.jframe.infrastructure.weixin;

import org.jframe.core.weixin.messaging.core.MessageBase;
import org.jframe.core.weixin.messaging.core.TemplateMessageBase;

/**
 * Created by Leo on 2017/10/31.
 */
public class WeixinMessageDto {
    private String json;
    private boolean isTemplate;

    public WeixinMessageDto(){

    }

    public WeixinMessageDto(String json, boolean isTemplate){
        this.json = json;
        this.isTemplate = isTemplate;
    }

    public WeixinMessageDto(MessageBase message){
        this.json = message.toJson();
        this.isTemplate = message instanceof TemplateMessageBase;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public boolean isTemplate() {
        return isTemplate;
    }

    public void setTemplate(boolean template) {
        isTemplate = template;
    }
}
