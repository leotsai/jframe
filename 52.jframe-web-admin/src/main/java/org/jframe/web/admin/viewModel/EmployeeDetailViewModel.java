package org.jframe.web.admin.viewModel;

import org.jframe.core.extensions.JList;
import org.jframe.core.extensions.JMap;
import org.jframe.data.entities.Department;
import org.jframe.data.entities.Employee;
import org.jframe.infrastructure.AppContext;
import org.jframe.service.admin.DepartmentService;
import org.jframe.service.admin.EmployeeService;
import org.jframe.web.admin.Menu;
import org.jframe.web.admin.score.AdminLayoutViewModel;

/**
 * created by yezi on 2018/1/19
 */
public class EmployeeDetailViewModel extends AdminLayoutViewModel {

    private Employee employee;
    private JList<Department> departments;
    private JMap<String, JMap<String,JList<String>>> rolePermissions;

    public EmployeeDetailViewModel build(Long employeeId){
        super.setCurrentPage(Menu.system().setting().permission());
        this.employee = AppContext.getBean(EmployeeService.class).get(employeeId);
        this.departments = AppContext.getBean(DepartmentService.class).getAll();
        this.rolePermissions = AppContext.getBean(EmployeeService.class).getRolePermissonsByUserId(employee.getUserId());
        return this;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public JMap<String, JMap<String,JList<String>>> getRolePermissions() {
        return rolePermissions;
    }

    public void setRolePermissions(JMap<String, JMap<String,JList<String>>> rolePermissions) {
        this.rolePermissions = rolePermissions;
    }

    public JList<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(JList<Department> departments) {
        this.departments = departments;
    }
}
