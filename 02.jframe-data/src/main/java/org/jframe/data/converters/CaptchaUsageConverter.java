package org.jframe.data.converters;


import org.jframe.infrastructure.sms.CaptchaUsage;

import javax.persistence.AttributeConverter;

/**
 * Created by leo on 2017-06-01.
 */
public class CaptchaUsageConverter implements AttributeConverter<CaptchaUsage, Integer> {
    @Override
    public Integer convertToDatabaseColumn(CaptchaUsage captchaUsage) {
        return captchaUsage.getValue();
    }

    @Override
    public CaptchaUsage convertToEntityAttribute(Integer integer) {
        return CaptchaUsage.from(integer);
    }
}
