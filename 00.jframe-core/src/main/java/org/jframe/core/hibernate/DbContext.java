package org.jframe.core.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.hibernate.transform.ResultTransformer;
import org.jframe.core.extensions.JList;
import org.jframe.core.extensions.JMap;
import org.jframe.core.extensions.PageRequest;
import org.jframe.core.extensions.PageResult;
import org.jframe.core.logging.LogHelper;

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
public class DbContext implements AutoCloseable {
    protected final Session session;
    private final Transformer transformer = new Transformer();
    private final HibernateSessionFactory sessionFactory;
    private boolean transactional;

    public DbContext(HibernateSessionFactory factory) {
        this(factory, false);
    }

    public DbContext(HibernateSessionFactory factory, boolean transactional) {
        this.sessionFactory = factory;
        this.session = factory.getFactory().openSession();
        this.transactional = transactional;
        if (transactional) {
            this.beginTransaction();
        }
    }

    public String getTableName(Class clazz) {
        return this.sessionFactory.getTableName(clazz);
    }

    public <T extends DbContext> T as(Class<T> clazz) {
        return (T) this;
    }

    public <T> DbSet<T> set(Class<T> clazz) {
        return new DbSet<T>(this, clazz);
    }

    public <T> JList<T> getList(String sql, Class<T> clazz, Object... parameterValues) {
        return this.getList(sql, clazz, this.getParameterMap(parameterValues));
    }

    public <T> JList<T> getList(String sql, Class<T> clazz, Map<String, Object> map) {
        Query query;
        boolean isEntity = clazz.getAnnotation(Entity.class) != null;
        if (isEntity) {
            query = this.session.createNativeQuery(sql, clazz);
        } else {
            query = this.session.createNativeQuery(sql).setResultTransformer(new DtoResultTransformer(clazz));
        }

        this.setParameters(query, map);
        return new JList<>(query.getResultList());
    }

    public <T> JList<T> getList(String sql, Object... parameterValues) {
        return this.getList(sql, this.getParameterMap(parameterValues));
    }

    public <T> JList<T> getList(String sql, Map<String, Object> map) {
        Query query = this.session.createNativeQuery(sql);
        this.setParameters(query, map);
        return new JList<>(query.getResultList());
    }

    public JList<JMap<String, Object>> getListMap(String sql, Map<String, Object> map) {
        Query query = this.session.createNativeQuery(sql);
        if (map != null) {
            this.setParameters(query, map);
        }
        query.unwrap(org.hibernate.query.Query.class).setResultTransformer(this.transformer);
        return new JList<>(query.getResultList());
    }

    public JList<JMap<String, Object>> getListMap(String sql, Object... parameterValues) {
        Map<String, Object> map = this.getParameterMap(parameterValues);
        return this.getListMap(sql, map);
    }

    public <T> T getFirst(String sql, Class<T> clazz, Object... parameterValues) {
        return this.getFirst(sql, clazz, this.getParameterMap(parameterValues));
    }

    public JMap<String, Object> getFirstMap(String sql, Object... parameterValues) {
        JList<JMap<String, Object>> list = this.getListMap(sql, parameterValues);
        return list.isEmpty() ? null : list.get(0);
    }

    public int execute(String sql, Map<String, Object> map) {
        Query query = this.session.createNativeQuery(sql);
        this.setParameters(query, map);
        return query.executeUpdate();
    }

    public int execute(String sql, Object... parameterValues) {
        return this.execute(sql, this.getParameterMap(parameterValues));
    }

    public <T> T getFirst(String sql, Class<T> clazz, Map<String, Object> map) {
        List<T> list = this.getList(sql, clazz, map);
        return list.isEmpty() ? null : list.get(0);
    }

    public <T> T getFirst(String sql, Object... parameterValues) {
        return this.getFirst(sql, this.getParameterMap(parameterValues));
    }

    public <T> T getFirst(String sql, Map<String, Object> map) {
        List<T> list = this.getList(sql, map);
        return list.isEmpty() ? null : list.get(0);
    }

    public int count(String sql, Object... parameterValues) {
        return this.count(sql, this.getParameterMap(parameterValues));
    }

    public int count(String sql, Map<String, Object> map) {
        NativeQuery query = this.session.createNativeQuery(sql);
        this.setParameters(query, map);
        BigInteger count = (BigInteger) query.getSingleResult();
        return count.intValue();
    }

    public <T> T get(Class clazz, Serializable id) {
        return (T) this.session.get(clazz, id);
    }

    public void delete(Object object) {
        this.session.delete(object);
    }

    public <T> PageResult<T> getPage(Class<T> clazz, String sql, PageRequest request, Object... parameterValues) {
        return this.getPage(clazz, sql, request, this.getParameterMap(parameterValues));
    }

    public <T> PageResult<T> getPage(Class<T> clazz, String sql, PageRequest request, Map<String, Object> parameters) {
        String sqlCount = "select count(1) from (" + sql + ") _tcount";
        String sqlPage = request.getSqlPageing(sql);
        int count = this.count(sqlCount, parameters);
        JList<T> page = this.getList(sqlPage, clazz, parameters);
        return new PageResult<>(request, count, page);
    }

    public Session getSession() {
        return this.session;
    }

    public Serializable save(Object o) {
        return this.session.save(o);
    }

    public Serializable saveAndCommit(Object o) {
        Serializable id = this.session.save(o);
        this.commitTransaction();
        return id;
    }

    @Deprecated
    public void update(Object o) {
        this.session.merge(o);
    }

    public Transaction beginTransaction() {
        Transaction transaction = this.session.getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
        }
        return transaction;
    }

    public void commitTransaction() {
        Transaction transaction = this.session.getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
        }
        try {
            transaction.commit();
        } catch (Throwable e) {
            LogHelper.error().log("__commitTransaction", e);
            throw e;
        }
    }

    public void rollback() {
        Transaction transaction = this.session.getTransaction();
        transaction.rollback();
    }

    protected void setParameters(Query query, Map<String, Object> map) {
        for (String key : map.keySet()) {
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
    public void close() {
        if (this.transactional) {
            Transaction transaction = this.session.getTransaction();
            if (TransactionStatus.ACTIVE == transaction.getStatus()) {
                transaction.rollback();
            }
        }
        this.session.close();
    }

    public void tryClose() {
        try {
            this.close();
        } catch (Exception ex) {
            LogHelper.error().log("DbContext.tryClose", ex);
        }
    }

    private class Transformer implements ResultTransformer {
        private static final long serialVersionUID = -6769467458218159371L;

        private Transformer() {
        }

        @Override
        public Object transformTuple(Object[] tuple, String[] aliases) {
            JMap map = new JMap();
            int i = 0;
            for (String name : aliases) {
                map.put(name, tuple[(i++)]);
            }
            return map;
        }

        @Override
        public List transformList(List collection) {
            return collection;
        }
    }

}
