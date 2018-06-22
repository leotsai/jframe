package org.jframe.core.extensions;

/**
 * Created by leo on 2017-06-01.
 */
public class Grouping<TKey, TEntity> {
    private TKey key;
    private JList<TEntity> list;

    public Grouping(){
        list = new JList<>();
    }

    public Grouping(TKey key, TEntity... items) {
        this();
        this.key = key;
        if (items != null) {
            for(TEntity item : items){
                this.list.add(item);
            }
        }
    }

    public TKey getKey() {
        return key;
    }

    public void setKey(TKey key) {
        this.key = key;
    }

    public JList<TEntity> getList() {
        return list;
    }

    public void setList(JList<TEntity> list) {
        this.list = list;
    }
}
