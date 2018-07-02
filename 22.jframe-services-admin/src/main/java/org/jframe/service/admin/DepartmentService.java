package org.jframe.service.admin;

import org.jframe.core.extensions.JList;
import org.jframe.data.entities.Department;

/**
 * created by yezi on 2018/1/16
 */
public interface DepartmentService {

    JList<Department> getAll();

    Department get(Long id);

    void save(Department department);

    void delete(Long id);

    void update(Long id, String name);

    Integer getDepartmentEmployeeCount(Long departmentId);

}
