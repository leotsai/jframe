package org.jframe.web.demo.score;

import org.jframe.core.extensions.JList;
import org.jframe.services.security.UserSession;
import org.jframe.web.demo.Menu;
import org.jframe.web.menu.MenuItem;
import org.jframe.web.security.WebContext;
import org.jframe.web.viewModels.LayoutViewModel;

/**
 * Created by Leo on 2018/1/17.
 */
public class DemoLayoutViewModel<T> extends LayoutViewModel {

    private JList<MenuItem> menu;
    private JList<MenuItem> topMenu;
    private JList<MenuItem> leftMenu;
    private MenuItem currentPage;

    public DemoLayoutViewModel() {

    }

    public DemoLayoutViewModel(String title) {
        super(title);
    }

    public DemoLayoutViewModel(String title, T value) {
        super(title, value);
    }

    private JList<MenuItem> getMenu() {
        if (menu != null) {
            return menu;
        }
        UserSession session = WebContext.getCurrent().getSession();
        if (session == null) {
            menu = new JList<>();
        } else {
            if (session.isSuperAdmin()) {
                menu = Menu.getInstance().getItems();
            } else {
                JList<String> permissions = session.getAllPermissions();
                menu = Menu.getInstance().getAuthorizedMenu(permissions);
            }
        }
        return menu;
    }


    private JList<MenuItem> getTopMenuItems() {
        return this.getMenu();
    }

    private JList<MenuItem> getLeftMenuItems() {
        if (this.currentPage == null) {
            return new JList<>();
        }
        for (MenuItem item : this.getMenu()) {
            if (item.getId().equals(this.currentPage.getParent().getParent().getId())) {
                return item.getChildren();
            }
        }
        return new JList<>();
    }


    public MenuItem getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(MenuItem currentPage) {
        this.currentPage = currentPage;
    }

    public JList<MenuItem> getTopMenu() {
        return this.getTopMenuItems();
    }

    public JList<MenuItem> getLeftMenu() {
        if (leftMenu == null) {
            leftMenu = this.getLeftMenuItems();
        }
        return this.getLeftMenuItems();
    }
}
