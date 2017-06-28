package org.jframe.infrastructure.core;


import java.util.*;
import java.util.function.Function;

/**
 * Created by leo on 2017-06-13.
 */
public class JList<T> extends ArrayList<T> {

    public JList() {

    }

    public JList(int initialCapacity) {
        super(initialCapacity);
    }

    public JList(Collection<T> list) {
        super(list);
    }

    public static <TEntity> JList<TEntity> from(TEntity[] array){
        return new JList<>(Arrays.asList(array));
    }

    public T firstOrNull(Function<T, Boolean> predicate) {
        if (predicate == null) {
            return this.firstOrNull();
        }
        for (T item : this) {
            if (predicate.apply(item)) {
                return item;
            }
        }
        return null;
    }

    public T firstOrNull() {
        if (this.size() == 0) {
            return null;
        }
        return this.get(0);
    }

    public boolean any(Function<T, Boolean> predicate) {
        for (T item : this) {
            if (predicate.apply(item)) {
                return true;
            }
        }
        return false;
    }

    public boolean all(Function<T, Boolean> predicate) {
        for (T item : this) {
            if (predicate.apply(item) == false) {
                return false;
            }
        }
        return true;
    }

    public JList<T> where(Function<T, Boolean> predicate) {
        JList<T> result = new JList<>();
        for (T item : this) {
            if (predicate.apply(item)) {
                result.add(item);
            }
        }
        return result;
    }

    public void removeAll(Function<T, Boolean> predicate) {
        JList<T> result = new JList<>();
        for (T item : this) {
            if (predicate.apply(item)) {
                result.add(item);
            }
        }
        this.removeAll(result);
    }

    public int count(Function<T, Boolean> predicate) {
        int count = 0;
        for (T item : this) {
            if (predicate.apply(item)) {
                count++;
            }
        }
        return count;
    }

    public <TKey> Map<TKey, JList<T>> toMap(Function<T, TKey> property) {
        Map<TKey, JList<T>> result = new HashMap<>();
        for (T item : this) {
            TKey key = property.apply(item);
            if (result.containsKey(key)) {
                result.get(key).add(item);
            } else {
                JList<T> children = new JList<>();
                children.add(item);
                result.put(key, children);
            }
        }
        return result;
    }

    public <TKey> JList<Grouping<TKey, T>> toGrouping(Function<T, TKey> property) {
        JList<Grouping<TKey, T>> result = new JList<>();
        for (T item : this) {
            TKey key = property.apply(item);
            Grouping grouping = result.firstOrNull(x -> x.getKey().equals(key));
            if (grouping != null) {
                grouping.getList().add(item);
            } else {
                result.add(new Grouping<>(key, item));
            }
        }
        return result;
    }

    public <TResult> JList<TResult> select(Function<T, TResult> predicate) {
        JList<TResult> result = new JList<>();
        for (T item : this) {
            result.add(predicate.apply(item));
        }
        return result;
    }

    public <TKey extends Comparable> OrderingJList<T> orderByAsc(Function<T, TKey> property){
        return new OrderingJList<>(this, property, OrderBy.asc);
    }

    public <TKey extends Comparable> OrderingJList<T> orderByDesc(Function<T, TKey> property){
        return new OrderingJList<>(this, property, OrderBy.desc);
    }

    public int sumInt(Function<T, Integer> property) {
        int result = 0;
        for (T item : this) {
            result += property.apply(item);
        }
        return result;
    }

    public int avgInt(Function<T, Integer> property) {
        return this.sumInt(property)/this.size();
    }

    public double sumDouble(Function<T, Double> property) {
        double result = 0;
        for (T item : this) {
            result += property.apply(item);
        }
        return result;
    }

    public double avgDouble(Function<T, Double> property){
        return this.sumDouble(property)/this.size();
    }

    public float sumFloat(Function<T, Float> property) {
        float result = 0;
        for (T item : this) {
            result += property.apply(item);
        }
        return result;
    }

    public float avgFloat(Function<T, Float> property){
        return this.sumFloat(property)/this.size();
    }


}
