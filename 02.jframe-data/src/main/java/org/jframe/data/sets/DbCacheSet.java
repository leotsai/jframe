package org.jframe.data.sets;

import org.jframe.core.extensions.JDate;
import org.jframe.core.hibernate.DbContext;
import org.jframe.core.hibernate.DbSet;
import org.jframe.core.logging.LogHelper;
import org.jframe.data.entities.DbCache;
import org.jframe.data.enums.DbCacheKey;

/**
 * Created by Leo on 2017/11/28.
 */
public class DbCacheSet extends DbSet<DbCache> {
    public DbCacheSet(DbContext db) {
        super(db, DbCache.class);
    }


    public void refresh(DbCacheKey key) {
        DbCache cache = super.getFirst("where cache_key=:p0", key.getValue());
        if (cache == null) {
            LogHelper.error().log("严重错误dbcache", key.toString());
            return;
        }
        cache.setVersion(JDate.now().getVersionSeconds());
        db.save(cache);
    }
}
