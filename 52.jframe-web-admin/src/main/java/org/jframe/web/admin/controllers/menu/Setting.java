package org.jframe.web.admin.controllers.menu;

import org.jframe.web.admin.controllers.menu.setting.Permission;
import org.jframe.web.menu.MenuItem;

/**
 * @author qq
 * @date 2018/6/26
 */
public class Setting extends MenuItem {
    public Setting() {
        super(2, "setting", "设置", "setting", "/admin/employee");
    }

    private final Permission permission = new Permission();

    public Permission permission() {
        return permission;
    }
}
