package org.jframe.core.hibernate;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.ResultTransformer;
import org.jframe.core.extensions.JList;
import org.jframe.core.extensions.JMap;
import org.jframe.core.extensions.KnownException;
import org.jframe.core.logging.LogHelper;

import java.util.List;

/**
 * Created by leo on 2017-05-26.
 */
public class DtoResultTransformer implements ResultTransformer {

    private final Class clazz;
    public DtoResultTransformer(Class clazz){
        this.clazz = clazz;
    }

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        try {
            DtoEntity entity = (DtoEntity) (this.clazz.newInstance());
            JMap<String, Object> map = new JMap<>();
            for(int i = 0; i < aliases.length; i++){
                map.put(StringUtils.lowerCase(aliases[i]), tuple[i]);
            }
            entity.fill(map);
            return entity;
        }
        catch (Exception ex){
            LogHelper.log("_transformTuple." + this.clazz.getName(), ex);
            return null;
        }
    }

    @Override
    public List transformList(List collection) {
        return collection;
    }

    public static void checkDtoEntityConstructors(JList<Class> dtoClasses){
        for(Class clazz : dtoClasses){
            Class[] interfaces = clazz.getInterfaces();
            for(Class interfaceClass : interfaces){
                if(interfaceClass.equals(DtoEntity.class)){
                    try{
                        clazz.newInstance();
                    }
                    catch (Exception ex){
                        throw new KnownException("检查DtoEntity的实现类是否具有无参构造函数，失败"+clazz.toString());
                    }
                }
            }
        }
    }

}
