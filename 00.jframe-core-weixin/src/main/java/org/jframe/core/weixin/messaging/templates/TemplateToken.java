package org.jframe.core.weixin.messaging.templates;

import org.jframe.core.extensions.Function0;

/**
 * Created by Leo on 2017/11/13.
 */
public class TemplateToken {
    private String key;
    private String description;
    private Function0<String> getValue;

    public TemplateToken(){

    }

    public TemplateToken(String key, String description, Function0<String> getValue){
        this.key = key;
        this.description = description;
        this.getValue = getValue;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Function0<String> getGetValue() {
        return getValue;
    }
}
