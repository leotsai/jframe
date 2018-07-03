package org.jframe.data.sets;

import org.jframe.core.extensions.JList;
import org.jframe.core.hibernate.DbContext;
import org.jframe.core.hibernate.DbSet;
import org.jframe.data.entities.Department;

/**
 * created by yezi on 2018/1/16
 */
public class DepartmentSet extends DbSet<Department> {

    public DepartmentSet(DbContext db) {
        super(db, Department.class);
    }

    @Override
    public JList<Department> getAll() {
        String sql = "select * from s_departments order by id desc ";
        return super.db.getList(sql,Department.class);
    }
}
