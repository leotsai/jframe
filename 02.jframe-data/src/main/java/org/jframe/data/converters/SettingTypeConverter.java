package org.jframe.data.converters;

import org.jframe.data.enums.SettingType;

import javax.persistence.AttributeConverter;

/**
 * @author qq
 * @date 2018/7/11
 */
public class SettingTypeConverter implements AttributeConverter<SettingType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(SettingType type) {
        return type.getValue();
    }

    @Override
    public SettingType convertToEntityAttribute(Integer integer) {
        return SettingType.from(integer);
    }
}
