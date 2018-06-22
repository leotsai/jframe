package org.jframe.data;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import org.hibernate.cfg.Configuration;
import org.jframe.core.extensions.JList;
import org.jframe.core.extensions.KnownException;
import org.jframe.core.hibernate.HibernateSessionFactory;

import java.io.File;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Created by leo on 2017-10-21.
 */
public class JframeHibernateSessionFactory extends HibernateSessionFactory {

    private final static JframeHibernateSessionFactory instance;

    public static JframeHibernateSessionFactory getInstance() {
        return instance;
    }

    static {
        instance = new JframeHibernateSessionFactory();
    }

    private String startupDirectory;
    private Properties properties;

    private JframeHibernateSessionFactory() {

    }

    public void initialize(String startupDirectory) {
        this.startupDirectory = startupDirectory;
        super.initialize();
    }

    public void initialize(Properties properties) {
        this.properties = properties;
        super.initialize();
    }

    @Override
    protected Configuration getConfiguration() {
        if (this.properties != null) {
            return new Configuration().setProperties(this.properties);
        }
        File file = Paths.get(this.startupDirectory, "WEB-INF/hibernate.cfg.xml").toFile();
        return new Configuration().configure(file);
    }

    @Override
    protected JList<Class> getEntityClasses() {


        ClassLoader classLoader = this.getClass().getClassLoader();
        try {
            ImmutableSet<ClassPath.ClassInfo> classInfos = ClassPath.from(classLoader).getTopLevelClassesRecursive("wpk.data.entities");
            if (classInfos.size() == 0) {
                throw new KnownException("找不到entity");
            }
            JList<Class> list = new JList<>();
            for (ClassPath.ClassInfo info : classInfos) {
                list.add(Class.forName(info.getName()));
            }
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new KnownException("初始化hibernate entities失败");
        }
    }
}
