package org.jframe.infrastructure.data;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.jframe.infrastructure.caching.AnnotationContext;
import org.jframe.infrastructure.extensions.JList;
import org.jframe.infrastructure.helpers.LogHelper;

import javax.persistence.Entity;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leo on 2017-05-18.
 */
public class DbContext implements AutoCloseable{
    protected final Session session;

    public DbContext(){
        this.session = HibernateSessionFactory.getInstance().getFactory().openSession();
    }

    public <T extends DbContext> T as(Class<T> clazz){
        return (T)this;
    }

    public <T> DbSet<T> set(Class<T> clazz){
        return new DbSet<T>(this, clazz);
    }

    public <T> JList<T> getList(String sql, Class<T> clazz, Object... parameterValues){
        return this.getList(sql, clazz, this.getParameterMap(parameterValues));
    }

    public <T> JList<T> getList(String sql, Class<T> clazz, Map<String, Object> map){
        Query query;
        boolean isEntity = AnnotationContext.get(clazz, Entity.class) != null;
        if(isEntity){
            query = this.session.createNativeQuery(sql, clazz);
        }
        else{
            query = this.session.createNativeQuery(sql).setResultTransformer(new DtoResultTransformer(clazz));
        }

        this.setParameters(query, map);
        return new JList<>(query.getResultList());
    }

    public <T> JList<T> getList(String sql, Object... parameterValues){
        return this.getList(sql, this.getParameterMap(parameterValues));
    }

    public <T> JList<T> getList(String sql, Map<String, Object> map){
        Query query = this.session.createNativeQuery(sql);
        this.setParameters(query, map);
        return new JList<>(query.getResultList());
    }

    public <T> T getFirst(String sql, Class<T> clazz, Object... parameterValues){
        return this.getFirst(sql, clazz, this.getParameterMap(parameterValues));
    }

    public int execute(String sql, Map<String, Object> map) {
        Query query = this.session.createNativeQuery(sql);
        this.setParameters(query, map);
        return query.executeUpdate();
    }

    public int execute(String sql, Object... parameterValues) {
        return this.execute(sql, this.getParameterMap(parameterValues));
    }

    public <T> T getFirst(String sql, Class<T> clazz, Map<String, Object> map){
        List<T> list = this.getList(sql, clazz, map);
        return list.isEmpty() ? null : list.get(0);
    }

    public <T> T getFirst(String sql, Object... parameterValues){
        return this.getFirst(sql ,this.getParameterMap(parameterValues));
    }

    public <T> T getFirst(String sql, Map<String, Object> map){
        List<T> list = this.getList(sql, map);
        return list.isEmpty() ? null : list.get(0);
    }

    public int count(String sql, Object... parameterValues){
        return this.count(sql, this.getParameterMap(parameterValues));
    }

    public int count(String sql, Map<String, Object> map){
        NativeQuery query = this.session.createNativeQuery(sql);
        this.setParameters(query, map);
        BigInteger count = (BigInteger) query.getSingleResult();
        return count.intValue();
    }

    public <T> T get(Class clazz, Serializable id){
        return (T)this.session.get(clazz, id);
    }

    public void delete(Object object){
        this.session.delete(object);
    }

    public <T> PageResult<T> getPage(Class clazz, String sql, PageRequest request, Object... parameterValues) {
        return this.getPage(clazz, sql, request, this.getParameterMap(parameterValues));
    }

    public <T> PageResult<T> getPage(Class clazz, String sql, PageRequest request, Map<String, Object> parameters) {
        String sqlCount = "select count(1) from (" + sql + ") _tcount";
        String sqlPage = request.getSqlPageing(sql);
        int count = this.count(sqlCount, parameters);
        JList<T> page = this.getList(sqlPage, clazz, parameters);
        return new PageResult<>(request, count, page);
    }

    public Session getSession(){
        return this.session;
    }

    public void save(Object o){
        this.session.save(o);
    }

    public void commitTransaction() {
        Transaction transaction = this.session.getTransaction();
        if(transaction.isActive() == false){
            transaction.begin();
        }
        transaction.commit();
    }

    protected void setParameters(Query query, Map<String, Object> map) {
        for(String key : map.keySet()){
            query.setParameter(key, map.get(key));
        }
    }

    protected Map<String, Object> getParameterMap(Object... parameterValues) {
        Map<String, Object> map = new HashMap<>();
        if (parameterValues == null || parameterValues.length == 0) {
            return map;
        }
        int index = 0;
        for (Object value : parameterValues) {
            map.put("p" + index, value);
            index++;
        }
        return map;
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
            LogHelper.log("DbContext.tryClose", ex);
        }
    }
}
