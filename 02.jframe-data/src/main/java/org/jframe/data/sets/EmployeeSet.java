package org.jframe.data.sets;


import org.jframe.core.extensions.JList;
import org.jframe.core.extensions.PageRequest;
import org.jframe.core.extensions.PageResult;
import org.jframe.core.hibernate.DbContext;
import org.jframe.core.hibernate.DbSet;
import org.jframe.data.entities.Employee;

/**
 * created by yezi on 2018/1/16
 */
public class EmployeeSet extends DbSet<Employee> {


    public EmployeeSet(DbContext db) {
        super(db, Employee.class);
    }

    public PageResult<Employee> searchByDepartmentId(PageRequest request, Long departmentId){
        return super.db.getPage(Employee.class,"select * from "+super.getTable()+" where department_id=:p0 order by id desc ",request,departmentId);
    }

    public JList<Employee> getByDepartmentId(Long departmentId){

        return super.db.getList("select * from "+super.getTable()+" where department_id=:p0 ",departmentId);
    }

    public Employee getByPhone(String phone){
        return super.getFirst(" where phone=:p0 ",phone);
    }

    public Integer getDepartmentEmployeeCount(Long departmentId){
        String sql = " select count(1) from s_employees where department_id=:p0 ";
        return super.db.count(sql,departmentId);
    }
}
