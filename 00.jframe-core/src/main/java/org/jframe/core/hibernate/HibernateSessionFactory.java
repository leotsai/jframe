package org.jframe.core.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.jframe.core.extensions.JList;
import org.jframe.core.extensions.KnownException;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 2017/5/17.
 */
public abstract class HibernateSessionFactory {

    private SessionFactory factory;

    private final Map<Class, String> entities;

    protected HibernateSessionFactory() {
        this.entities = new HashMap<>();
    }

    public String getTableName(Class clazz) {
        return this.entities.get(clazz);
    }

    public SessionFactory getFactory() {
        if (this.factory == null) {
            throw new KnownException("请先调用" + this.getClass().getName() + "的initialize方法");
        }
        return factory;
    }

    public void close() {
        this.factory.close();
    }

    public void initialize() {
        Configuration configuration = this.getConfiguration();
        if(configuration == null){
            System.out.println(this.getClass().getName()+" configuration is null, initialization aborted!");
            return;
        }
        JList<Class> list = this.getEntityClasses();
        for (Class clazz : list) {
            Entity entity = (Entity) clazz.getAnnotation(Entity.class);
            if (entity == null) {
                continue;
            }
            Table table = (Table) clazz.getAnnotation(Table.class);
            if (table == null) {
                throw new KnownException(clazz.getName() + "需要添加@Table注解");
            }
            configuration.addAnnotatedClass(clazz);
            this.entities.put(clazz, table.name());
        }
        try {
            factory = configuration.buildSessionFactory();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        System.out.println(this.getClass().getName() + " hibernate initialized");
    }

    protected abstract Configuration getConfiguration();

    protected abstract JList<Class> getEntityClasses();


}
