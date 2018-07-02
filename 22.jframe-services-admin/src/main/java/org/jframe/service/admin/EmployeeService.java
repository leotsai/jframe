package org.jframe.service.admin;

import org.jframe.core.extensions.JList;
import org.jframe.core.extensions.JMap;
import org.jframe.core.extensions.PageRequest;
import org.jframe.core.extensions.PageResult;
import org.jframe.data.entities.Employee;
import org.jframe.service.admin.dto.EmployeeDto;
import org.jframe.service.admin.dto.EmployeeEditDto;


/**
 * created by yezi on 2018/1/16
 */
public interface EmployeeService {

    PageResult<EmployeeDto> searchByDepartmentId(PageRequest request, Long departmentId);

    void save(Employee employee, String cvsRoleIds);

    Employee get(Long id);

    Employee getByPhone(String phone);

    EmployeeEditDto getById(Long id);

    JMap<String, JMap<String, JList<String>>> getRolePermissonsByUserId(Long userId);

    void delete(Long employeeId);

    Integer getSuperAdminCount();
}
