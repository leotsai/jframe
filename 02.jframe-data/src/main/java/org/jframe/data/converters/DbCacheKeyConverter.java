package org.jframe.data.converters;

import org.jframe.data.enums.DbCacheKey;

import javax.persistence.AttributeConverter;

/**
 * Created by Leo on 2017/9/18.
 */
public class DbCacheKeyConverter implements AttributeConverter<DbCacheKey, Integer> {
    @Override
    public Integer convertToDatabaseColumn(DbCacheKey cacheKey) {
        return cacheKey.getValue();
    }

    @Override
    public DbCacheKey convertToEntityAttribute(Integer integer) {
        return DbCacheKey.from(integer);
    }
}
