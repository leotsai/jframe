package org.jframe.data.converters;

import org.jframe.core.logging.enums.LogArea;

import javax.persistence.AttributeConverter;

/**
 * created by yezi on 2018/7/9
 */
public class LogTypeConverter implements AttributeConverter<LogArea, Integer> {

    @Override
    public Integer convertToDatabaseColumn(LogArea logArea) {
        return logArea.getValue();
    }

    @Override
    public LogArea convertToEntityAttribute(Integer integer) {
        return LogArea.from(integer);
    }
}
