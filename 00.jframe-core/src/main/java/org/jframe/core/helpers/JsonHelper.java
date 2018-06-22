package org.jframe.core.helpers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.jframe.core.extensions.JList;

/**
 * Created by Leo on 2017/1/9.
 */
public class JsonHelper {
    public static <T> T tryDeserialize(String json, Class<T> clazz) {
        try {
            return JSON.parseObject(json, clazz);
        } catch (Exception ex) {
            return null;
        }
    }

    public static <T> T deserialize(String json, Class<T> clazz) {
        if (StringHelper.isNullOrWhitespace(json)) {
            return null;
        }
        return JSON.parseObject(json, clazz);
    }

    public static <T> JList<T> deserializeList(String json, Class<T> clazz){
        if (StringHelper.isNullOrWhitespace(json)){
            return null;
        }
        return new JList<>(JSON.parseArray(json, clazz));
    }

    public static String serialize(Object object){
        if(object == null){
            return null;
        }
        return JSON.toJSONString(object);
    }

    public static String serialize(Object object, SerializerFeature... features) {
        if (object == null) {
            return null;
        }
        if (features == null || features.length == 0) {
            return JSON.toJSONString(object);
        }
        return JSON.toJSONString(object, features);
    }
}
