package org.jframe.data.caching;

import org.jframe.data.enums.DbCacheKey;

/**
 * Created by Leo on 2017/11/28.
 */
public interface VersionedCacheContext {
    long getVersion();

    void refresh(long version);

    void initialize(long version);

    DbCacheKey getCacheKey();
}
