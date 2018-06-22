package org.jframe.core.weixin.pay;

import org.jframe.core.helpers.StringHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 2018/1/9.
 */
public class WxPayHelper {

    public static Map<String, String> filterEmptyValues(Map<String, String> map) {
        Map<String, String> result = new HashMap<>();
        for (String key : map.keySet()) {
            String value = map.get(key);
            if (!StringHelper.isNullOrWhitespace(value)) {
                result.put(key, value);
            }
        }
        return result;
    }

}
