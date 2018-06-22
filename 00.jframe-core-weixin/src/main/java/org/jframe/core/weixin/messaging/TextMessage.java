package org.jframe.core.weixin.messaging;

import org.jframe.core.helpers.JsonHelper;
import org.jframe.core.weixin.messaging.core.MessageBase;

/**
 * Created by Leo on 2017/10/31.
 */
public class TextMessage extends MessageBase {


    private String text;

    public TextMessage(String openId, String text) {
        super(openId);
        this.text = text;
    }

    @Override
    public String toJson() {
        JsonDto dto = new JsonDto();
        dto.touser = this.getToUserOpenId();
        dto.text.content = this.text;
        return JsonHelper.serialize(dto);
    }

    private class JsonDto {
        private String touser;
        private String msgtype = "text";
        private JsonDtoText text = new JsonDtoText();

        public String getTouser() {
            return touser;
        }

        public String getMsgtype() {
            return msgtype;
        }

        public JsonDtoText getText() {
            return text;
        }
    }

    private class JsonDtoText {
        private String content;

        public String getContent() {
            return content;
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
