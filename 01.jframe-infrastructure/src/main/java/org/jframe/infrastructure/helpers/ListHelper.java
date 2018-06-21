package org.jframe.infrastructure.helpers;

import org.jframe.infrastructure.extensions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 2017/1/9.
 */
public class ListHelper {
    public static <T> T firstOrNull(Iterable<T> list, SafeFunc1<T, Boolean> predicate) {
        if(list == null){
            return null;
        }
        if(predicate == null){
            return list.iterator().next();
        }
        for (T item : list) {
            if (predicate.invoke(item)) {
                return item;
            }
        }
        return null;
    }

    public static <T> boolean any(Iterable<T> list, SafeFunc1<T, Boolean> predicate){
        if(list == null){
            return false;
        }
        for (T item : list) {
            if (predicate.invoke(item)) {
                return true;
            }
        }
        return false;
    }

    public static <T1, T2> boolean any(Iterable<T1> list1, Iterable<T2> list2, SafeFunc2<T1, T2, Boolean> predicate){
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

    public static <T> List<T> where(List<T> list, SafeFunc1<T, Boolean> predicate) {
        List<T> result = new ArrayList<>();
        if (list == null) {
            return result;
        }
        for (T item : list) {
            if (predicate.invoke(item)) {
                result.add(item);
            }
        }
        return result;
    }

}
