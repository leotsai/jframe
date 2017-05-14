package org.jframe.infrastructure.helpers;

import com.alibaba.fastjson.JSON;

/**
 * Created by Leo on 2017/1/9.
 */
public class JsonHelper {
    public static <T> T tryDeserialize(String json, Class<T> clazz){
        try{
            return JSON.parseObject(json, clazz);
        }
        catch (Exception ex){
            return null;
        }
    }

    public static <T> T deserialize(String json, Class<T> clazz){
        return JSON.parseObject(json, clazz);
    }

    public static String serialize(Object object){
        if(object == null){
            return null;
        }
        return JSON.toJSONString(object);
    }

}
