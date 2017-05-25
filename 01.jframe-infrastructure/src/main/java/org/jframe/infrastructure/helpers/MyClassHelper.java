package org.jframe.infrastructure.helpers;

import org.apache.commons.io.FileUtils;
import org.jframe.AppContext;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by leo on 2017-05-18.
 */
public class MyClassHelper {

    public static List<Class> getClasses(String basePackage)
    {
        String packageDir = basePackage.replace(".","/");
        File root = new File(AppContext.getRootFolder());
        Collection<File> classFiles =  FileUtils.listFiles(root, new String[]{"class"}, true);
        Collection<File> jarFiles =  FileUtils.listFiles(root, new String[]{"jar"}, true);

        List<Class> clazzs = new ArrayList<Class>();
        for(File jar : jarFiles){
            clazzs.addAll(getClassesFromJar(jar, packageDir));
        }
        return clazzs;
    }

    private static List<Class> getClassesFromJar(File jarPath, String basePackage){
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
