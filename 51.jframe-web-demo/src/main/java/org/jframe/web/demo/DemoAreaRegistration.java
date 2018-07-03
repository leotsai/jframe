package org.jframe.web.demo;

import org.jframe.core.extensions.JList;
import org.jframe.core.extensions.KnownException;
import org.jframe.core.helpers.StringHelper;
import org.jframe.core.logging.LogHelper;
import org.jframe.infrastructure.AppContext;
import org.jframe.core.web.AreaRegistration;
import org.jframe.web.core.PermissionDefinition;
import org.jframe.web.menu.MenuItem;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by Leo on 2018/1/17.
 */
@Component
public class DemoAreaRegistration implements AreaRegistration {
    @Override
    public void run() throws Exception {
        this.initMenu();
    }

    private void initMenu() throws Exception {
        JList<String> codes = this.registerMenuAndPermissions();
        Menu.getInstance().initialize();
        this.checkIfAnyCodeNotRegisteredToMenu(codes);
    }

    private void checkIfAnyCodeNotRegisteredToMenu(JList<String> codes) {
        for (MenuItem item1 : Menu.getInstance().getItems()) {
            codes.removeAll(item1.getPermissions());
            for (MenuItem item2 : item1.getChildren()) {
                codes.removeAll(item2.getPermissions());
                for (MenuItem item3 : item2.getChildren()) {
                    codes.removeAll(item3.getPermissions());
                }
            }
        }
        if (codes.size() > 0) {
            String message = "\n\n警告：有如下权限代码为注册到菜单：" + String.join(",", codes) + "\n\n";
            LogHelper.logRaw("代码错误", message);
            System.err.println(message);
        }
    }

    private JList<String> registerMenuAndPermissions() throws Exception {
        JList<String> allCodes = new JList<>();
        Map<String, PermissionDefinition> map = AppContext.getBeans(PermissionDefinition.class);
        for (String key : map.keySet()) {
            if (!key.startsWith("org.jframe.web.demo.controllers.")) {
                continue;
            }
            PermissionDefinition definition = map.get(key);
            definition.registerMenuPermissions();
            Class innerClass = definition.getClass();
            for (Field field : innerClass.getFields()) {
                allCodes.add(field.get(null).toString());
            }
        }
        this.validateDuplicatePermissions(allCodes);
        return allCodes;
    }

    private void validateDuplicatePermissions(JList<String> codes) {
        StringBuilder sb = new StringBuilder();
        codes.toGroupedMap(x -> x).forEach((x, y) -> {
            if (y.size() > 1) {
                sb.append("\n" + x + " 重复数：" + y.size());
            }
        });
        String error = sb.toString();
        if (!StringHelper.isNullOrWhitespace(error)) {
            error = "\n\n代码中定义了重复的权限。错误内容如下：" + error;
            LogHelper.logRaw("代码错误", error);
            System.err.println(error);
            throw new KnownException("\n\n严重代码错误！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！\n\n");
        }
    }

}
