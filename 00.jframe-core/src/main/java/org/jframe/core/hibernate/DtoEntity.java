package org.jframe.core.hibernate;


import org.jframe.core.extensions.JMap;

/**
 * Created by leo on 2017-05-26.
 */
public interface DtoEntity {
    void fill(JMap<String, Object> row);
}
