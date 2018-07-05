package org.jframe.web.admin;

import org.jframe.core.extensions.JList;
import org.jframe.core.helpers.ClassHelper;
import org.jframe.core.helpers.StringHelper;
import org.jframe.web.admin.menu.Home;
import org.jframe.web.admin.menu.System;
import org.jframe.web.menu.MenuItem;

import java.lang.reflect.Method;
import java.util.function.Predicate;

/**
 * Created by Leo on 2018/1/17.
 */
public class Menu {

    private static Home home = new Home();
    private static System system = new System();

    public static Home home() {
        return home;
    }

    public static System system() {
        return system;
    }


    private final static Menu instance = new Menu();

    public static Menu getInstance() {
        return instance;
    }

    private JList<MenuItem> items = new JList<>();

    private Menu() {

    }

    public JList<MenuItem> getItems() {
        return this.items;
    }

    public JList<MenuItem> getClonedItems() {
        return getAuthorizedMenu(this.items, null);
    }

    public void initialize() {
        for (Method method1 : this.getClass().getDeclaredMethods()) {
            MenuItem item1 = ClassHelper.tryInvoke(MenuItem.class, method1, null);
            if (item1 == null) {
                continue;
            }
            this.items.add(item1);
            JList<MenuItem> tempItem2s = new JList<>();
            for (Method method2 : item1.getClass().getDeclaredMethods()) {
                MenuItem item2 = ClassHelper.tryInvoke(MenuItem.class, method2, item1);
                if (item2 == null) {
                    continue;
                }
                item2.setParent(item1);
                tempItem2s.add(item2);
                JList<MenuItem> tempItem3s = new JList<>();
                for (Method method3 : item2.getClass().getDeclaredMethods()) {
                    MenuItem item3 = ClassHelper.tryInvoke(MenuItem.class, method3, item2);
                    if (item3 != null) {
                        item3.setParent(item2);
                        tempItem3s.add(item3);
                    }
                }
                item2.getChildren().addAll(tempItem3s.orderByAsc(x -> x.getOrder()).toList());
            }
            item1.getChildren().addAll(tempItem2s.orderByAsc(x -> x.getOrder()).toList());
        }
        this.items = this.items.orderByAsc(x -> x.getOrder()).toList();
    }

    public JList<MenuItem> getAuthorizedMenu(JList<String> sessionPermissions) {
        return getAuthorizedMenu(this.items, x -> x.getPermissions().any(p1 -> sessionPermissions.contains(p1)));
    }

    private static JList<MenuItem> getAuthorizedMenu(JList<MenuItem> items, Predicate<MenuItem> matchPermission) {
        JList<MenuItem> newList = new JList<>();
        for (MenuItem item1 : items) {
            MenuItem newItem1 = item1.clone();
            String item1url = null;
            for (MenuItem item2 : item1.getChildren()) {
                JList<MenuItem> pages = matchPermission == null ? item2.getChildren() : item2.getChildren().where(matchPermission);
                if (pages.size() > 0) {
                    MenuItem newItem2 = item2.clone();
                    newItem2.getChildren().addAll(pages.select(p -> p.clone()));
                    newItem1.getChildren().add(newItem2);
                    if (StringHelper.isNullOrWhitespace(item1url)) {
                        item1url = pages.get(0).getUrl();
                    }
                }
            }
            newItem1.setUrl(item1url);
            if (newItem1.getChildren().size() > 0) {
                newList.add(newItem1);
            }
        }
        return newList;
    }

}
