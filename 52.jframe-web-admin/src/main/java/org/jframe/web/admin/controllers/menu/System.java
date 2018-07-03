package org.jframe.web.admin.controllers.menu;

import org.jframe.web.admin.controllers.menu.system.Setting;
import org.jframe.web.admin.controllers.menu.system.setting.Permission;
import org.jframe.web.menu.MenuItem;

/**
 * @author qq
 * @date 2018/6/26
 */
public class System extends MenuItem {
    public System() {
        super(2, "system", "系统", "system", "/admin/employee");
    }

    private final Setting setting = new Setting();

    public Setting setting() {
        return setting;
    }
}
