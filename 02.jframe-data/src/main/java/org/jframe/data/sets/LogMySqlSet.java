package org.jframe.data.sets;

import org.jframe.core.hibernate.DbContext;
import org.jframe.core.hibernate.DbSet;
import org.jframe.data.entities.LogMySql;

/**
 * created by yezi on 2018/7/9
 */
public class LogMySqlSet extends DbSet<LogMySql> {

    public LogMySqlSet(DbContext db) {
        super(db, LogMySql.class);
    }


}
