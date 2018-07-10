package org.jframe.service.admin.impl;

import org.jframe.core.extensions.JList;
import org.jframe.core.exception.KnownException;
import org.jframe.data.entities.Department;
import org.jframe.data.entities.Employee;
import org.jframe.service.admin.DepartmentService;
import org.jframe.services.core.ServiceBase;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * created by yezi on 2018/1/16
 */
@Service
public class DepartmentServiceImpl extends ServiceBase implements DepartmentService {
    @Override
    public JList<Department> getAll() {
        return super.getFromDb(db -> db.getDepartmentSet().getAll());
    }

    @Override
    public Department get(Long id) {
        return super.getFromDb(db -> db.getDepartmentSet().get(id));
    }

    @Override
    public void save(Department department) {
        super.useTransaction(db -> {
            if (department.isToCreate()) {
                db.save(department);
            } else {
                Department dbDepartment = db.getDepartmentSet().get(department.getId());
                if (dbDepartment == null) {
                    throw new KnownException("参数异常");
                }
                dbDepartment.updateName(department.getName());
                db.save(db);
            }
        });
    }

    @Override
    public void delete(Long id) {
        super.useTransaction(db -> {
            Department dbDepartment = db.getDepartmentSet().get(id);
            if (Objects.isNull(dbDepartment)) {
                throw new KnownException("部门数据不存在");
            }
            JList<Employee> departments = db.getEmployeeSet().getByDepartmentId(id);
            if (!Objects.isNull(departments) && departments.size() > 0) {
                throw new KnownException("请将该部门成员转移其它部门或删除成员后再进行删除");
            }
            db.delete(dbDepartment);
        });
    }

    @Override
    public void update(Long id, String name) {
        super.useTransaction(db -> {
            Department dbDepartment = db.getDepartmentSet().get(id);
            if (dbDepartment == null) {
                throw new KnownException("参数异常");
            }
            dbDepartment.updateName(name);
            db.save(dbDepartment);
        });
    }

    @Override
    public Integer getDepartmentEmployeeCount(Long departmentId) {
        return super.getFromDb(db -> db.getEmployeeSet().getDepartmentEmployeeCount(departmentId));
    }
}
