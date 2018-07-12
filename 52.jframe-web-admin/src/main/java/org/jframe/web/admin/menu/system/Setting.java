package org.jframe.web.admin.menu.system;

import org.jframe.web.admin.menu.system.setting.Configuration;
import org.jframe.web.admin.menu.system.setting.Permission;
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
    private final Configuration configuration = new Configuration();

    public Permission permission() {
        return permission;
    }

    public Configuration config() {
        return configuration;
    }
}
