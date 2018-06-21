package org.jframe.data.converters;

import org.jframe.data.enums.Gender;
import org.jframe.infrastructure.extensions.JList;

import javax.persistence.AttributeConverter;

/**
 * Created by leo on 2017-06-28.
 */
public class GenderConverter implements AttributeConverter<Gender, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Gender gender) {
        return gender.getValue();
    }

    @Override
    public Gender convertToEntityAttribute(Integer integer) {
        return JList.from(Gender.values()).firstOrNull(x -> x.getValue() == integer);
    }
}
