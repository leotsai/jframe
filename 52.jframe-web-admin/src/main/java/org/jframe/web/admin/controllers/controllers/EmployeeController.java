package org.jframe.web.admin.controllers.controllers;

import org.jframe.core.extensions.JList;
import org.jframe.core.extensions.KnownException;
import org.jframe.core.extensions.PageRequest;
import org.jframe.core.helpers.StringHelper;
import org.jframe.core.web.RestPost;
import org.jframe.core.web.StandardJsonResult;
import org.jframe.data.entities.Employee;
import org.jframe.data.entities.Permission;
import org.jframe.service.admin.EmployeeService;
import org.jframe.services.UserService;
import org.jframe.services.security.UserSession;
import org.jframe.web.core.PermissionDefinition;
import org.jframe.web.admin.controllers.Menu;
import org.jframe.web.admin.controllers.viewModel.EmployeeDetailViewModel;
import org.jframe.web.admin.controllers.viewModel.EmployeeEditViewModel;
import org.jframe.web.admin.controllers.viewModel.EmployeeIndexViewModel;
import org.jframe.web.security.Authorize;
import org.jframe.web.security.WebContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

/**
 * created by yezi on 2018/1/16
 */
@Controller
@RequestMapping("/admin/employee")
public class EmployeeController extends _AdminControllerBase {

    @Component
    public final class Codes implements PermissionDefinition {

        public final static String SELECT = "admin-employee-select";
        public final static String EDIT = "admin-employee-edit";
        public final static String DELETE = "admin-employee-delete";

        @Override
        public void registerDefinitions(JList<Permission> permissions) {
            permissions.add(new Permission("系统", "安全管理", "添加/编辑部门成员", EDIT));
            permissions.add(new Permission("系统", "安全管理", "查看角色设置", SELECT));
            permissions.add(new Permission("系统", "安全管理", "添加/修改角色", DELETE));
        }

        @Override
        public void registerMenuPermissions() {
            Menu.demo().permission().apc(SELECT, EDIT, DELETE);
        }
    }

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UserService userService;

//    @Autowired
//    private AdminSettingService adminSettingService;


    @RequestMapping("")
    @Authorize(permissions = {Codes.SELECT})
    public ModelAndView index() {
        return super.tryView("admin-employee-index", () -> new EmployeeIndexViewModel().build(), mv -> {
            UserSession session = WebContext.getCurrent().getSession();
            mv.addObject("superAdmin", session.isSuperAdmin());
            mv.addObject("canSeeRole", session.hasPermission(Codes.SELECT));
            mv.addObject("canEditRole", session.hasPermission(Codes.EDIT));
            mv.addObject("canAddDepartment", session.hasPermission(DepartmentController.Codes.ADDDEPARTMENT));
            mv.addObject("canEditDepartment", session.hasPermission(DepartmentController.Codes.EDITDEPARTMENT));
            mv.addObject("canDeleteDepartment", session.hasPermission(DepartmentController.Codes.DELETEDEPARTMENT));
            mv.addObject("canAddEmployee", session.hasPermission(Codes.EDIT));
        });
    }

    @RequestMapping("/list")
    @Authorize(permissions = {Codes.SELECT})
    public ModelAndView list(PageRequest pageRequest, Long departmentId) {
        return super.tryView("admin-employee-list", () -> employeeService.searchByDepartmentId(pageRequest, departmentId));
    }

    @RequestMapping("/edit")
    @Authorize(permissions = {Codes.EDIT})
    public ModelAndView edit(Long employeeId, Long departmentId) {
        return super.tryView("admin-employee-edit", () -> new EmployeeEditViewModel().build(employeeId, departmentId));
    }

    @RequestMapping("/detail")
    @Authorize(permissions = {Codes.SELECT})
    public ModelAndView detail(Long employeeId) {
        return super.tryView("admin-employee-detail", () -> new EmployeeDetailViewModel().build(employeeId), mv -> {
            UserSession session = WebContext.getCurrent().getSession();
            mv.addObject("canEditEmployee", session.hasPermission(Codes.EDIT));
            mv.addObject("canDeleteEmployee", session.hasPermission(Codes.DELETE));
        });
    }

    @RestPost("/matchPhone")
    @Authorize(permissions = {Codes.EDIT})
    public StandardJsonResult matchPhone(String phone) {
        return super.tryJson(() -> {
            if (!Objects.isNull(employeeService.getByPhone(phone))) {
                throw new KnownException("该用户已绑定部门成员");
            }
            return userService.get(phone);
        });
    }

    @RestPost("/save")
    @Authorize(permissions = {Codes.EDIT})
    public StandardJsonResult save(Employee employee, String cvsRoleIds) {
        return super.tryJson(() -> {
            StringHelper.validate_notNullOrWhitespace(employee.getPhone(), "手机号码不能为空");
            StringHelper.validate_notNullOrWhitespace(employee.getName(), "真实姓名不能为空");
            Long userId = employee.getUserId();
            employeeService.save(employee, cvsRoleIds);
            WebContext.refreshSession(userId);
        });
    }

    @RestPost("/delete")
    @Authorize(permissions = {Codes.DELETE})
    public StandardJsonResult delete(Long employeeId) {
        return super.tryJson(() -> {
            Employee employee = employeeService.get(employeeId);
            employeeService.delete(employeeId);
            WebContext.refreshSession(employee.getUserId());
        });
    }

    @RestPost("/getSuperAdminCount")
    @Authorize(permissions = {Codes.SELECT})
    public StandardJsonResult getCount() {
        return super.tryJson(() -> employeeService.getSuperAdminCount());
    }


}
