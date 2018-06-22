package org.jframe.core.helpers;

import org.apache.commons.io.FileUtils;
import org.jframe.core.extensions.JList;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by leo on 2017-05-18.
 */
public class ClassHelper {

    public static <T> T tryNewInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception ex) {
            return null;
        }
    }

    public static <T> T tryInvoke(Class<T> clazz, Method method, Object object) {
        try {
            Object value = method.invoke(object);
            if(value == null){
                return null;
            }
            if(clazz.isInstance(value)){
                return (T) value;
            }
            return null;
        } catch (Throwable ex) {
            return null;
        }
    }

    public static Object newInnerInstance(Class outerClass, Class innerClass) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Constructor constructor = innerClass.getDeclaredConstructor(outerClass);
        return constructor.newInstance(outerClass.newInstance());
    }

    public static JList<Class> getClasses(String directory, String basePackage) {
        String packageDir = basePackage.replace(".", "/");
        File root = new File(directory);
        Collection<File> classFiles = FileUtils.listFiles(root, new String[]{"class"}, true);
        Collection<File> jarFiles = FileUtils.listFiles(root, new String[]{"jar"}, true);

        JList<Class> clazzs = new JList<>();
        for (File jar : jarFiles) {
            clazzs.addAll(getClassesFromJar(jar, packageDir));
        }
        return clazzs;
    }

    private static List<Class> getClassesFromJar(File jarPath, String basePackage) {
        List<Class> clazzs = new ArrayList<Class>();
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(jarPath);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        List<JarEntry> jarEntryList = new ArrayList<JarEntry>();

        Enumeration<JarEntry> ee = jarFile.entries();
        while (ee.hasMoreElements()) {
            JarEntry entry = (JarEntry) ee.nextElement();
            if (entry.getName().startsWith(basePackage) && entry.getName().endsWith(".class")) {
                jarEntryList.add(entry);
            }
        }
        for (JarEntry entry : jarEntryList) {
            String className = entry.getName().replace('/', '.');
            className = className.substring(0, className.length() - 6);

            try {
                clazzs.add(Thread.currentThread().getContextClassLoader().loadClass(className));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return clazzs;
    }

}
