package org.jframe.web.admin.controllers.viewModel;

import org.jframe.core.extensions.JList;
import org.jframe.data.entities.Department;
import org.jframe.data.entities.Role;
import org.jframe.data.enums.Gender;
import org.jframe.infrastructure.AppContext;
import org.jframe.service.admin.DepartmentService;
import org.jframe.service.admin.EmployeeService;
import org.jframe.service.admin.RoleService;
import org.jframe.service.admin.dto.EmployeeEditDto;
import org.jframe.web.admin.controllers.Menu;
import org.jframe.web.admin.controllers.score.AdminLayoutViewModel;

import java.util.Objects;

/**
 * created by yezi on 2018/1/19
 */
public class EmployeeEditViewModel extends AdminLayoutViewModel {

    private EmployeeEditDto employee;
    private Department department;
    private JList<Department> departments;
    private JList<Gender> genders;
    //    private JMap<Role, JMap<String, JList<String>>> allRolePermissions;
    private JList<Role> systemRoles;
    private JList<Role> userRoles;
    private JList<Role> selectedRoles;

    public EmployeeEditViewModel() {
        super("新增/修改成员");
    }

    public EmployeeEditViewModel build(Long employeeId, Long departmentId) {
        super.setCurrentPage(Menu.setting().permission());
        RoleService roleService = AppContext.getBean(RoleService.class);
        if (!Objects.isNull(employeeId)) {
            this.employee = AppContext.getBean(EmployeeService.class).getById(employeeId);
            this.selectedRoles = roleService.getAllByUserId(this.employee.getUserId());
        }
        if (!Objects.isNull(departmentId)) {
            this.department = AppContext.getBean(DepartmentService.class).get(departmentId);
        }
        JList<Role> allRoles = roleService.getAll();
        this.systemRoles = allRoles.where(x -> x.isSystem() && !Objects.equals(x.getName(), Role.Names.SUPER_ADMIN));
        this.userRoles = allRoles.where(x -> !x.isSystem());
        this.departments = AppContext.getBean(DepartmentService.class).getAll();
        this.genders = JList.from(Gender.values());
        return this;
    }

    public EmployeeEditDto getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeEditDto employee) {
        this.employee = employee;
    }

    public JList<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(JList<Department> departments) {
        this.departments = departments;
    }

    public JList<Gender> getGenders() {
        return genders;
    }

    public void setGenders(JList<Gender> genders) {
        this.genders = genders;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public JList<Role> getSystemRoles() {
        return systemRoles;
    }

    public void setSystemRoles(JList<Role> systemRoles) {
        this.systemRoles = systemRoles;
    }

    public JList<Role> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(JList<Role> userRoles) {
        this.userRoles = userRoles;
    }

    public JList<Role> getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(JList<Role> selectedRoles) {
        this.selectedRoles = selectedRoles;
    }
}
