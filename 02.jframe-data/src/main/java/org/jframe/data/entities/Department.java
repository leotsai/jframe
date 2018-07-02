package org.jframe.data.entities;

import org.jframe.data.core.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Leo on 2018/1/16.
 */
@Entity
@Table(name = "s_departments")
public class Department extends EntityBase {

    @Column(name = "name", columnDefinition = "varchar(50) not null COMMENT '部门名称'")
    private String name;

    public void updateName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
