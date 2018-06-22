package org.jframe.core.aliyun.sms;

import org.jframe.core.extensions.JList;

/**
 * Created by leo on 2017-11-12.
 */
public interface SmsTemplate {
    JList<String> getPhoneNumbers();
    String getSignName();
    String getTemplateCode();
    String toJson();
}
