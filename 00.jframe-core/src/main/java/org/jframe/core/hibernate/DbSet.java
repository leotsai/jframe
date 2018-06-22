package org.jframe.core.hibernate;

import org.jframe.core.extensions.JDate;
import org.jframe.core.extensions.JList;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Map;

/**
 * Created by leo on 2017-05-18.
 */
public class DbSet<T> {

    protected final DbContext db;
    protected final Class<T> clazz;

    public DbSet(DbContext db, Class<T> clazz) {
        this.db = db;
        this.clazz = clazz;
    }

    public T get(Serializable id) {
        return this.db.get(clazz, id);
    }

    public JList<T> getAll() {
        String sql = "select * from " + this.getTable();
        return this.db.getList(sql, this.clazz);
    }

    public <TId extends Serializable> JList<T> getAll(JList<TId> ids) {
        if (ids == null || ids.size() == 0) {
            return new JList<>();
        }
        String sql = "select * from " + this.getTable() + " where id in :p0";
        return this.db.getList(sql, this.clazz, ids);
    }

    public T getFirst(String where, Object... parameterValues) {
        String sql = "select * from " + this.getTable() + " " + where;
        return this.db.getFirst(sql, this.clazz, parameterValues);
    }

    public JList<T> getList(String where, Object... parameterValues) {
        String sql = "select * from " + this.getTable() + " " + where;
        return this.db.getList(sql, this.clazz, parameterValues);
    }

    public JList<T> getList(String where, Map<String, Object> params) {
        String sql = "select * from " + this.getTable() + " " + where;
        return this.db.getList(sql, this.clazz, params);
    }

    public int count() {
        String sql = "select count(1) from " + this.getTable();
        return this.db.count(sql);
    }

    public int count(String where, Object... parameterValues) {
        String sql = "select count(1) from " + this.getTable() + " " + where;
        return this.db.count(sql, parameterValues);
    }

    public Long getMaxIdTillYesterday() {
        String sql = "select max(id) from " + this.getTable() + " where created_time<:p0";
        BigInteger value = this.db.getFirst(sql, JDate.today().toDateString());
        return value == null ? 0L : value.longValue();
    }

    public String getTable() {
        return this.db.getTableName(this.clazz);
    }

}
