package org.jframe.core.weixin.messaging.core;

import org.jframe.core.extensions.JList;
import org.jframe.core.weixin.messaging.templates.TemplateToken;

/**
 * Created by leo on 2017-09-24.
 */
public abstract class TemplateMessageBase extends MessageBase {

    private String jsonTemplate;

    public TemplateMessageBase(){

    }

    protected TemplateMessageBase(String openId, String jsonTemplate){
        super.setToUserOpenId(openId);
        this.jsonTemplate = jsonTemplate;
    }

    public JList<TemplateToken> getTokens(){
        JList<TemplateToken> tokens = new JList<>();
        tokens.add(new TemplateToken("${openId}", "open id", ()->this.getToUserOpenId()));
        return tokens;
    }

    @Override
    public String toJson(){
        String json = this.jsonTemplate;
        JList<TemplateToken> tokens = this.getTokens();
        for(TemplateToken token : tokens){
            json = json.replace(token.getKey(), token.getGetValue().apply());
        }
        return json;
    }


    public String getJsonTemplate() {
        return jsonTemplate;
    }

    public void setJsonTemplate(String jsonTemplate) {
        this.jsonTemplate = jsonTemplate;
    }
}
