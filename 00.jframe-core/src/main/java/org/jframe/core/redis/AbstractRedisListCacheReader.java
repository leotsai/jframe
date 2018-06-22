package org.jframe.core.redis;


import org.jframe.core.extensions.JList;
import org.jframe.core.helpers.JsonHelper;
import org.jframe.core.helpers.StringHelper;

import java.util.Objects;

/**
 * Created by leo on 2017-10-01.
 */
public abstract class AbstractRedisListCacheReader<T> extends RedisCacheReaderBase<T> {

    protected abstract JList<T> internalReadFromDb();

    public AbstractRedisListCacheReader(Class<T> clazz) {
        super(clazz);
    }

    @Override
    public JList<T> read() {
        String value = super.getRedisValue();
        if (!StringHelper.isNullOrWhitespace(value) && !Objects.equals(value,"[]")) {
            return JsonHelper.deserializeList(value, this.getClazz());
        }
        return (JList<T>) super.getFromDbAndSetCache();
    }

    @Override
    protected JList<T> readFromDb() {
        return this.internalReadFromDb();
    }


}
