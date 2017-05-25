package org.jframe.infrastructure.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by leo on 2017-05-18.
 */
public class DbSet<T> {

    protected final DbContext db;
    protected final Class<T> clazz;
    public DbSet(DbContext db, Class<T> clazz){
        this.db = db;
        this.clazz = clazz;
    }


    public T get(Serializable id){
        return this.db.get(clazz, id);
    }

    public List<T> getAll(){
        String sql = "select * from " + this.getTable();
        return this.db.getList(sql, this.clazz);
    }

    public T getFirst(String where, Object... parameterValues){
        String sql = "select * from " + this.getTable() + where;
        return this.db.getFirst(sql, this.clazz, parameterValues);
    }

    public List<T> getList(String where, Object... parameterValues){
        String sql = "select * from " + this.getTable() + where;
        return this.db.getList(sql, this.clazz, parameterValues);
    }

    public int count(){
        String sql = "select count(1) from " + this.getTable();
        return this.db.count(sql, this.clazz);
    }

    public int count(String where, Object... parameterValues){
        String sql = "select count(1) from " + this.getTable() + where;
        return this.db.count(sql, parameterValues);
    }

    public String getTable(){
        return HibernateSessionFactory.getInstance().getTableName(this.clazz) + " ";
    }


}
