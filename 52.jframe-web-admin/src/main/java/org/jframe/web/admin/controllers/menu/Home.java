package org.jframe.web.admin.controllers.menu;

import org.jframe.web.admin.controllers.menu.setting.Permission;
import org.jframe.web.menu.MenuItem;

/**
 * @author qq
 * @date 2018/6/26
 */
public class Home extends MenuItem {
    public Home() {
        super(1, "home", "首页", "setting", "/admin");
    }
}
