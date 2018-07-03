package org.jframe.web.admin.controllers.menu.system;

import org.jframe.web.admin.controllers.menu.system.setting.Permission;
import org.jframe.web.menu.MenuItem;

/**
 * @author:qq
 * @date:2018/1/18
 */
public class Setting extends MenuItem {
    public Setting() {
        super(1, "l2Setting", "设置", "setting", "#");
    }

    private final Permission permission = new Permission();

    public Permission permission() {
        return permission;
    }
}
