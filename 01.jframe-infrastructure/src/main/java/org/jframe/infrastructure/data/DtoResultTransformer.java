package org.jframe.infrastructure.data;

import org.hibernate.transform.ResultTransformer;
import org.jframe.infrastructure.helpers.ClassHelper;
import org.jframe.infrastructure.helpers.LogHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            Map<String, Object> map = new HashMap<>();
            for(int i = 0; i < aliases.length; i++){
                map.put(aliases[i], tuple[i]);
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

    public static void checkDtoEntityConstructors(){
        List<Class> classList = ClassHelper.getClasses("org.jframe");
        for(Class clazz : classList){
            Class[] interfaces = clazz.getInterfaces();
            for(Class interfaceClass : interfaces){
                if(interfaceClass.equals(DtoEntity.class)){
                    try{
                        clazz.newInstance();
                    }
                    catch (Exception ex){
                        LogHelper.log("_checkDtoEntityConstructors","检查DtoEntity的实现类是否具有无参构造函数，失败：" + clazz.toString());
                    }
                }
            }
        }
        System.out.print("检查DtoEntity的实现类是否具有无参构造函数，通过\n\n");
    }

}
