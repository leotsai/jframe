package org.jframe.web.admin.controllers;

import org.jframe.core.extensions.JList;
import org.jframe.core.web.RestPost;
import org.jframe.core.web.StandardJsonResult;
import org.jframe.data.entities.Department;
import org.jframe.data.entities.Permission;
import org.jframe.service.admin.DepartmentService;
import org.jframe.web.admin.Menu;
import org.jframe.web.core.PermissionDefinition;
import org.jframe.web.security.Authorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * created by yezi on 2018/1/16
 */
@Controller
@RequestMapping("/admin/department")
public class DepartmentController extends _AdminControllerBase {

    @Component
    public final class Codes implements PermissionDefinition {
        public final static String ADDDEPARTMENT = "admin-department-add";
        public final static String EDITDEPARTMENT = "admin-department-edit";
        public final static String DELETEDEPARTMENT = "admin-department-delete";

        @Override
        public void registerDefinitions(JList<Permission> permissions) {
            permissions.add(new Permission("系统", "安全管理", "添加部门", ADDDEPARTMENT));
            permissions.add(new Permission("系统", "安全管理", "编辑部门", EDITDEPARTMENT));
            permissions.add(new Permission("系统", "安全管理", "删除部门", DELETEDEPARTMENT));
        }

        @Override
        public void registerMenuPermissions() {
            Menu.system().setting().permission().apc(ADDDEPARTMENT, EDITDEPARTMENT, DELETEDEPARTMENT);
        }
    }

    @Autowired
    private DepartmentService departmentService;

    @RestPost("/save")
    @Authorize(permissions = {Codes.ADDDEPARTMENT})
    public StandardJsonResult save(Department department) {
        return super.tryJson(() -> departmentService.save(department));
    }

    @RestPost("/getDepartmentEmployeeCount")
    @Authorize(permissions = {EmployeeController.Codes.SELECT})
    public StandardJsonResult getDepartmentEmployeeCount(Long departmentId) {
        return super.tryJson(() -> departmentService.getDepartmentEmployeeCount(departmentId));
    }

    @RestPost("/delete")
    @Authorize(permissions = {Codes.DELETEDEPARTMENT})
    public StandardJsonResult delete(Long id) {
        return super.tryJson(() -> departmentService.delete(id));
    }

    @RestPost("/update")
    @Authorize(permissions = {Codes.EDITDEPARTMENT})
    public StandardJsonResult update(Long id, String name) {
        return super.tryJson(() -> departmentService.update(id, name));
    }

}
