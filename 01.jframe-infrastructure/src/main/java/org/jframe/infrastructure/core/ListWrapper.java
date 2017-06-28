package org.jframe.infrastructure.core;

import java.util.List;

/**
 * Created by leo on 2017-05-26.
 */
public class ListWrapper<T> {
    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
