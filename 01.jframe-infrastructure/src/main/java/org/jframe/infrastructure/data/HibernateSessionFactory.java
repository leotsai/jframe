package org.jframe.infrastructure.data;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.jframe.AppContext;
import org.jframe.infrastructure.helpers.ClassHelper;

import javax.persistence.Entity;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by screw on 2017/5/17.
 */
public class HibernateSessionFactory {

    private SessionFactory factory;

    private static HibernateSessionFactory instance = new HibernateSessionFactory();

    public static HibernateSessionFactory getInstance() {
        return instance;
    }

    private final Map<Class, String> entities;

    private HibernateSessionFactory() {
        this.entities = new HashMap<>();
    }

    public String getTableName(Class clazz){
        return this.entities.get(clazz);
    }

    public SessionFactory getFactory() {
        if (factory != null) {
            return factory;
        }
        this.initialize();
        return factory;
    }

    public void initialize(){
        File file = new File(AppContext.getRootFolder() + "\\WEB-INF\\hibernate.cfg.xml");
        Configuration configuration = new Configuration().configure(file);
        List<Class> list = ClassHelper.getClasses("org.jframe.data.entities");
        for(Class clz : list) {
            configuration.addAnnotatedClass(clz);
            Entity entity = (Entity) clz.getAnnotation(Entity.class);
            if(entity != null) {
                this.entities.put(clz, entity.name());
            }
        }
        factory = configuration.buildSessionFactory();
    }


}
