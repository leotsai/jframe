package org.jframe.infrastructure.helpers;

import org.jframe.infrastructure.core.*;

/**
 * Created by Leo on 2017/1/9.
 */
public class ListHelper {
    public static <T> T firstOrNull(T[] list, Func2<T, Boolean> predicate) {
        if(list == null){
            return null;
        }
        for (T item : list) {
            if (predicate.invoke(item)) {
                return item;
            }
        }
        return null;
    }

    public static <T1, T2> boolean any(T1[] list1, T2[] list2, Func3<T1, T2, Boolean> predicate){
        if(list1 == null || list2 == null){
            return false;
        }
        for (T1 item1 : list1) {
            for(T2 item2 : list2){
                if (predicate.invoke(item1, item2)) {
                    return true;
                }
            }
        }
        return false;
    }

}
