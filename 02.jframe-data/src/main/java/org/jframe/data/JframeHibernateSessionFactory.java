package org.jframe.data;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import org.hibernate.cfg.Configuration;
import org.jframe.core.app.AppInitializer;
import org.jframe.core.extensions.JList;
import org.jframe.core.extensions.KnownException;
import org.jframe.core.helpers.ClassHelper;
import org.jframe.core.hibernate.DtoResultTransformer;
import org.jframe.core.hibernate.HibernateSessionFactory;
import org.jframe.infrastructure.AppContext;

import java.io.File;
import java.nio.file.Paths;
import java.util.Date;

/**
 * Created by leo on 2017-10-21.
 */
public class JframeHibernateSessionFactory extends HibernateSessionFactory implements AppInitializer {

    private final static JframeHibernateSessionFactory instance;

    public static JframeHibernateSessionFactory getInstance() {
        return instance;
    }

    static {
        instance = new JframeHibernateSessionFactory();
    }

    private JframeHibernateSessionFactory() {

    }

    @Override
    public String init() {
        String packageName = AppContext.getAppConfig().getEntityPackage() + ".";
        JList<Class> classes = ClassHelper.getClasses(AppContext.getStartupDirectory(), packageName);
        DtoResultTransformer.checkDtoEntityConstructors(classes);
        super.initialize();
        return this.getClass().getName() + " initialize success!";
    }

    @Override
    protected Configuration getConfiguration() {
        File file = Paths.get(AppContext.getStartupDirectory(), "WEB-INF/hibernate.cfg.xml").toFile();
        return new Configuration().configure(file);
    }

    @Override
    protected JList<Class> getEntityClasses() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        try {
            ImmutableSet<ClassPath.ClassInfo> classInfos = ClassPath.from(classLoader).getTopLevelClassesRecursive(AppContext.getAppConfig().getEntityPackage());
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
