package org.jframe.web.admin.controllers.controllers;

import org.jframe.core.extensions.JList;
import org.jframe.core.web.RestPost;
import org.jframe.core.web.StandardJsonResult;
import org.jframe.data.entities.Permission;
import org.jframe.data.entities.Role;
import org.jframe.data.entities.User;
import org.jframe.infrastructure.AppContext;
import org.jframe.service.admin.RoleSettingService;
import org.jframe.service.admin.dto.RolePermissionDto;
import org.jframe.services.UserService;
import org.jframe.services.security.UserSession;
import org.jframe.web.admin.controllers.Menu;
import org.jframe.web.core.PermissionDefinition;
import org.jframe.web.security.Authorize;
import org.jframe.web.security.WebContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author luoh
 * @date 2018/1/18 16:33
 */
@Controller
@RequestMapping("/admin/roleSetting")
public class RoleSettingController extends _AdminControllerBase {

    @Component
    public final class Codes implements PermissionDefinition {

        public final static String DICT = "admin-permissionDict-dict";

        @Override
        public void registerDefinitions(JList<Permission> permissions) {
            permissions.add(new Permission("系统", "安全管理", "浏览权限字典", DICT));

        }

        @Override
        public void registerMenuPermissions() {
            Menu.system().setting().permission().apc(DICT);
        }
    }

    @Autowired
    private RoleSettingService service;

    @Authorize(permissions = EmployeeController.Codes.SELECT)
    @RequestMapping("/getPermissions")
    public ModelAndView getPermissions(Long id) {
        return super.tryView("admin-roleSetting-permission", () -> {
            RolePermissionDto permissions = service.getPermissions(id);
            return permissions;
        }, mv -> {
            UserSession userSession = WebContext.getCurrent().getSession();
            mv.addObject("canEditRole", userSession.hasPermission(EmployeeController.Codes.EDIT));
            mv.addObject("canSeeDict", userSession.hasPermission(Codes.DICT));
        });
    }

    @Authorize(permissions = EmployeeController.Codes.EDIT)
    @RestPost("/savePermission")
    public StandardJsonResult savePermission(Role role) {
        return super.tryJson(() -> {
            service.savePermission(role);
            JList<User> users = AppContext.getBean(UserService.class).getUsersByRoleId(role.getId());
            users.forEach(x -> {
                WebContext.refreshSession(x.getUsername());
            });
        });
    }

//    @Authorize(permissions = EmployeeController.Codes.EDIT_ROLE)
//    @RestPost("/saveRole")
//    public StandardJsonResult saveRole(Role role) {
//        if (role == null) {
//            throw new KnownException("数据错误，请刷新重试");
//        }
//        return super.tryJson(() -> service.saveRole(role));
//    }
}
