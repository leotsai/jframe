package org.jframe.infrastructure.sms.templates;

import org.jframe.core.aliyun.sms.SmsTemplate;
import org.jframe.infrastructure.sms.CaptchaUsage;

/**
 * @author qq
 * @date 2018/7/11
 */
public interface SmsCaptchaTemplate extends SmsTemplate {
    CaptchaUsage getUsage();
    String getCode();
}
