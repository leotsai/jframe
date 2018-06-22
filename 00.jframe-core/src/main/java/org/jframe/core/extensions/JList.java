package org.jframe.core.extensions;


import org.jframe.core.helpers.StringHelper;
import org.jframe.core.web.SelectItem;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

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

    public static <TEntity> JList<TEntity> from(TEntity[] array) {
        if (array == null) {
            return new JList<>();
        }
        return new JList<>(Arrays.asList(array));
    }

    public static <TKey, TValue> JList<KeyValuePair<TKey, TValue>> fromMap(Map<TKey, TValue> map) {
        JList<KeyValuePair<TKey, TValue>> list = new JList<>();
        for (TKey key : map.keySet()) {
            list.add(new KeyValuePair<>(key, map.get(key)));
        }
        return list;
    }

    public static <TEntity> JList<TEntity> from(Collection<TEntity> collection) {
        return new JList<>(collection);
    }

    public static JList<String> splitByComma(String input) {
        if (StringHelper.isNullOrEmpty(input)) {
            return new JList<>();
        }
        return JList.from(input.split(","));
    }

    public T firstOrNull(Predicate<T> predicate) {
        if (predicate == null) {
            return this.firstOrNull();
        }
        for (T item : this) {
            if (predicate.test(item)) {
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

    public T lastOrNull(Predicate<T> predicate) {
        if (predicate == null) {
            return this.lastOrNull();
        }
        for (int i = this.size() - 1; i >= 0; i--) {
            T item = this.get(i);
            if (predicate.test(item)) {
                return item;
            }
        }
        return null;
    }

    public T lastOrNull() {
        if (this.size() == 0) {
            return null;
        }
        return this.get(this.size() - 1);
    }

    public boolean any(Predicate<T> predicate) {
        return this.stream().anyMatch(predicate);
    }

    public boolean all(Predicate<T> predicate) {
        for (T item : this) {
            if (predicate.test(item) == false) {
                return false;
            }
        }
        return true;
    }

    public JList<T> where(Predicate<T> predicate) {
        JList<T> result = new JList<>();
        for (T item : this) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        return result;
    }

    public JList<SelectItem> toSelectItems(Function<T, String> valueProperty, Function<T, String> textProperty) {
        return this.toSelectItems(valueProperty, textProperty, null);
    }

    public JList<SelectItem> toSelectItems(Function<T, String> valueProperty, Function<T, String> textProperty, String optionalLabel) {
        JList<SelectItem> list = this.select(x -> new SelectItem(valueProperty.apply(x), textProperty.apply(x)));
        if (optionalLabel != null) {
            list.add(0, new SelectItem("", optionalLabel));
        }
        return list;
    }

    public void removeAll(Predicate<T> predicate) {
        JList<T> result = new JList<>();
        for (T item : this) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        this.removeAll(result);
    }

    public int count(Predicate<T> predicate) {
        int count = 0;
        for (T item : this) {
            if (predicate.test(item)) {
                count++;
            }
        }
        return count;
    }

    public <TKey> Map<TKey, JList<T>> toGroupedMap(Function<T, TKey> property) {
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

    public <TKey, TValue> JMap<TKey, TValue> toMap(Function<T, TKey> keyProperty, Function<T, TValue> valueProperyt) {
        JMap<TKey, TValue> result = new JMap<>();
        for (T item : this) {
            TKey key = keyProperty.apply(item);
            TValue value = valueProperyt.apply(item);
            result.put(key, value);
        }
        return result;
    }

    public String joinString(CharSequence delimiter, Function<T, String> property) {
        return String.join(delimiter, this.select(property));
    }

    public <TKey> JList<Grouping<TKey, T>> toGrouping(Function<T, TKey> property) {
        JList<Grouping<TKey, T>> result = new JList<>();
        for (T item : this) {
            TKey key = property.apply(item);
            Grouping grouping = result.firstOrNull(x -> Objects.equals(x.getKey(), key));
            if (grouping != null) {
                grouping.getList().add(item);
            } else {
                result.add(new Grouping<>(key, item));
            }
        }
        return result;
    }

    public T[] toArray(Class<T> clazz) {
        T[] copy = (T[]) Array.newInstance(clazz, this.size());
        for (int i = 0; i < this.size(); i++) {
            copy[i] = this.get(i);
        }
        return copy;
    }

    public <TResult> JList<TResult> select(Function<T, TResult> predicate) {
        JList<TResult> result = new JList<>();
        for (T item : this) {
            result.add(predicate.apply(item));
        }
        return result;
    }

    public <TResult> JList<TResult> selectMany(Function<T, Collection<TResult>> predicate) {
        JList<TResult> result = new JList<>();
        for (T item : this) {
            result.addAll(predicate.apply(item));
        }
        return result;
    }

    public <TKey extends Comparable> OrderingJList<T> orderByAsc(Function<T, TKey> property) {
        return new OrderingJList<>(this, property, OrderBy.asc);
    }

    public <TKey extends Comparable> OrderingJList<T> orderByDesc(Function<T, TKey> property) {
        return new OrderingJList<>(this, property, OrderBy.desc);
    }

    public <TValue> TValue sum(Class<TValue> clazz, Function<T, TValue> valueProperty, Function2<TValue, TValue, TValue> add) {
        TValue result = null;
        for (T item : this) {
            TValue value = valueProperty.apply(item);
            if (result == null) {
                result = value;
            } else {
                result = add.apply(result, value);
            }
        }
        return result;
    }

    public int sumInt(Function<T, Integer> property) {
        int result = 0;
        for (T item : this) {
            result += property.apply(item);
        }
        return result;
    }

    public long sumLong(Function<T, Long> property) {
        long result = 0L;
        for (T item : this) {
            result += property.apply(item);
        }
        return result;
    }

    public BigDecimal sumDecimal(Function<T, BigDecimal> valueProperty) {
        BigDecimal result = BigDecimal.ZERO;
        for (T item : this) {
            result = result.add(valueProperty.apply(item));
        }
        return result;
    }

    public int avgInt(Function<T, Integer> property) {
        return this.sumInt(property) / this.size();
    }

    public double sumDouble(Function<T, Double> property) {
        double result = 0;
        for (T item : this) {
            result += property.apply(item);
        }
        return result;
    }

    public double avgDouble(Function<T, Double> property) {
        return this.sumDouble(property) / this.size();
    }

    public float sumFloat(Function<T, Float> property) {
        float result = 0;
        for (T item : this) {
            result += property.apply(item);
        }
        return result;
    }

    public float avgFloat(Function<T, Float> property) {
        return this.sumFloat(property) / this.size();
    }

    public JList<T> skip(int count) {
        JList<T> list = new JList<>();
        for (int i = count; i < this.size(); i++) {
            list.add(this.get(i));
        }
        return list;
    }

    public JList<T> take(int count) {
        if (this.size() <= count) {
            return this;
        }
        return new JList<>(this.subList(0, count));
    }

    public JList<JList<T>> randomCombination(int take) {
        return this.randomArrangementOrCombination(this, take, false);
    }

    public JList<JList<T>> randomArrangement(int take) {
        return this.randomArrangementOrCombination(this, take, true);
    }

    private JList<JList<T>> randomArrangementOrCombination(JList<T> list, int take, boolean isArrangement) {
        JList<JList<T>> result = new JList<>();
        if (list.size() < take || take <= 0) {
            return result;
        }
        if (take == 1) {
            for (T item : list) {
                JList<T> childList = new JList<>();
                childList.add(item);
                result.add(childList);
            }
            return result;
        }
        for (int i = 0; i < list.size(); i++) {
            T item = list.get(i);
            JList<T> newList = isArrangement ? new JList<>(list) : list.skip(i + 1);
            if (isArrangement) {
                newList.remove(i);
            }
            JList<JList<T>> childResult = randomArrangementOrCombination(newList, take - 1, isArrangement);
            for (JList<T> childList : childResult) {
                childList.add(0, item);
                result.add(childList);
            }
        }
        return result;
    }

    public <TResult> JList<TResult> distinct(Function<T, TResult> function) {
        JList<TResult> result = new JList<>();
        for (T item : this) {
            TResult tResult = function.apply(item);
            if (!result.contains(tResult)) {
                result.add(tResult);
            }
        }
        return result;
    }

}
