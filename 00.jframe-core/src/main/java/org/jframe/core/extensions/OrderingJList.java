package org.jframe.core.extensions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by leo on 2017-06-13.
 */
public class OrderingJList<T> {

    private final JList<T> list;
    private final List<SortItem<? extends Comparable>> sorts;

    public <TKey extends Comparable> OrderingJList(JList<T> list, Function<T, TKey> property, OrderBy orderBy) {
        this.list = list;
        this.sorts = new ArrayList<>();
        this.sorts.add(new SortItem(property, orderBy));
    }

    public <TKey extends Comparable> OrderingJList<T> thenByAsc(Function<T, TKey> property){
        this.sorts.add(new SortItem(property, OrderBy.asc));
        return this;
    }

    public <TKey extends Comparable> OrderingJList<T> thenByDesc(Function<T, TKey> property){
        this.sorts.add(new SortItem(property, OrderBy.desc));
        return this;
    }

    public JList<T> toList() {
        for (int i = 0; i < this.list.size(); i++) {
            T item1 = this.list.get(i);
            for (int j = i + 1; j < this.list.size(); j++) {
                T item2 = this.list.get(j);
                if (this.needsToSwap(item1, item2)) {
                    this.list.set(i, item2);
                    this.list.set(j, item1);
                    item1 = item2;
                }
            }
        }
        return this.list;
    }

    private boolean needsToSwap(T item1, T item2) {
        if (this.sorts.size() == 0) {
            return false;
        }
        for (SortItem<? extends Comparable> sort : this.sorts) {
            int comparedValue = sort.property.apply(item1).compareTo(sort.property.apply(item2));
            if(comparedValue > 0){
                return sort.orderBy == OrderBy.asc;
            }
            if(comparedValue < 0){
                return sort.orderBy == OrderBy.desc;
            }
        }
        return false;
    }

    private class SortItem<TKey extends Comparable>{
        private final Function<T, TKey> property;
        private final OrderBy orderBy;

        public SortItem(Function<T, TKey> property, OrderBy orderBy){
            this.property = property;
            this.orderBy = orderBy;
        }


        public Function<T, TKey> getProperty() {
            return property;
        }

        public OrderBy getOrderBy() {
            return orderBy;
        }
    }

}
