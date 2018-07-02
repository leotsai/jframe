package org.jframe.web.admin.controllers.menu;

import org.jframe.web.admin.controllers.menu.demo.Permission;
import org.jframe.web.menu.MenuItem;

/**
 * @author qq
 * @date 2018/6/26
 */
public class Demo extends MenuItem {
    public Demo() {
        super(1, "demo", "DEMO", "demo", "/admin");
    }

    private final Permission permission = new Permission();

    public Permission permission() {
        return permission;
    }
}
