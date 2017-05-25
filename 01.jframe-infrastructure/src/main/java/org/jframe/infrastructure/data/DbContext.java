package org.jframe.infrastructure.data;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by leo on 2017-05-18.
 */
public class DbContext implements AutoCloseable{
    protected final Session session;
    protected Transaction transaction;

    public DbContext(){
        this.session = HibernateSessionFactory.getInstance().getFactory().openSession();
    }

    public <T extends DbContext> T as(Class<T> clazz){
        return (T)this;
    }

    public <T> DbSet<T> set(Class<T> clazz){
        return new DbSet<T>(this, clazz);
    }

    public <T> List<T> getList(String sql, Class<T> clazz, Object... parameterValues){
        Query query = this.session.createNativeQuery(sql, clazz);
        this.setParameters(query, parameterValues);
        return query.getResultList();
    }

    public <T> List<T> getList(String sql, Object... parameterValues){
        Query query = this.session.createNativeQuery(sql);
        this.setParameters(query, parameterValues);
        return query.getResultList();
    }

    public <T> T getFirst(String sql, Class<T> clazz, Object... parameterValues){
        List<T> list = this.getList(sql, clazz, parameterValues);
        return list.isEmpty() ? null : list.get(0);
    }

    public <T> T getFirst(String sql, Object... parameterValues){
        List<T> list = this.getList(sql, parameterValues);
        return list.isEmpty() ? null : list.get(0);
    }

    public int count(String sql, Object... parameterValues){
        NativeQuery query = this.session.createNativeQuery(sql);
        this.setParameters(query, parameterValues);
        BigInteger count = (BigInteger) query.getSingleResult();
        return count.intValue();
    }

    public <T> T get(Class clazz, Serializable id){
        return (T)this.session.get(clazz, id);
    }


    public Session getSession(){
        return this.session;
    }

    public Transaction getTransaction(){
        if(this.transaction == null){
            this.transaction = this.session.getTransaction();
        }
        return this.transaction;
    }

    public void save(Object o){
        this.session.save(o);
    }

    public void commitTransaction() {
        if (this.transaction == null) {
            this.transaction = this.session.getTransaction();
            this.transaction.begin();
        }
        this.getTransaction().commit();
    }

    protected void setParameters(Query query, Object... parameterValues) {
        if (parameterValues != null) {
            int index = 0;
            for (Object value : parameterValues) {
                query.setParameter("p" + index, value);
                index++;
            }
        }
    }


    @Override
    public void close() throws Exception {
        this.session.close();
    }

    public void tryClose(){
        try{
            this.close();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


}
