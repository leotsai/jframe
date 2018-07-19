package org.jframe.web.security;

import org.jframe.core.app.AppInitializer;
import org.jframe.core.exception.KnownException;
import org.jframe.core.extensions.JList;
import org.jframe.core.helpers.StringHelper;
import org.jframe.core.logging.LogHelper;
import org.jframe.data.JframeDbContext;
import org.jframe.data.entities.Permission;
import org.jframe.data.entities.Role;
import org.jframe.infrastructure.AppContext;
import org.jframe.web.core.PermissionDefinition;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Leo on 2018/1/12.
 */
public class PermissionRegistery implements AppInitializer {
    private static final PermissionRegistery instance = new PermissionRegistery();

    public static PermissionRegistery getInstance() {
        return instance;
    }

    private PermissionRegistery() {

    }

    @Override
    public String init() {
        String result = initAllPermissions();
        return this.getClass().getName() + " initialize success!\n    " + result;
    }

    @Override
    public void close() {

    }

    public String initAllPermissions() {
        try {
            return doWork();
        } catch (Exception ex) {
            throw new KnownException("系统-初始化PermissionRegistery,失败");
        }
    }

    private String doWork() throws Exception {
        JList<Permission> permissions = getPermissions();
        validateDuplicatePermissions(permissions);

        StringBuilder sb = new StringBuilder("初始化PermissionRegistery完成。");
        try (JframeDbContext db = new JframeDbContext(true)) {
            JList<Permission> dbPermissions = db.getPermissionSet().getAll();
            JList<Permission> added = permissions.where(n -> !dbPermissions.any(d -> d.getCode().equals(n.getCode())));
            JList<Permission> deleted = dbPermissions.where(d -> !permissions.any(n -> d.getCode().equals(n.getCode())));
            JList<Permission> updated = dbPermissions.where(d -> permissions.any(n -> d.isNameOrGroupChanged(n)));

            added.forEach(a -> db.save(a));
            deleted.forEach(a -> db.delete(a));
            updated.forEach(u -> {
                Permission newPermission = permissions.firstOrNull(x -> Objects.equals(x.getCode(), u.getCode()));
                u.setName(newPermission.getName());
                u.setGroup(newPermission.getGroup());
                u.setSubGroup(newPermission.getSubGroup());
                db.save(u);
            });
            sb.append("新增权限：" + added.size() + "，删除权限：" + deleted.size() + "，更新名称和分组：" + updated.size() + "。");

            JList<String> allCodes = permissions.select(x -> x.getCode());
            JList<Role> roles = db.getRoleSet().getAll().where(x -> x.isSystem() == false);
            for (Role role : roles) {
                JList<String> roleCodes = role.getCodes();
                JList<String> missingCodes = roleCodes.where(x -> !allCodes.contains(x));
                if (missingCodes.size() > 0) {
                    roleCodes.removeAll(missingCodes);
                    role.setCodes(roleCodes);
                    db.save(role);
                    sb.append("\n角色（" + role.getName() + "）删除无效权限：" + String.join(",", missingCodes) + "。");
                }
            }
            db.commitTransaction();
        }
        return sb.toString();
    }

    private void validateDuplicatePermissions(JList<Permission> permissions) {
        StringBuilder sb = new StringBuilder();
        permissions.toGroupedMap(x -> x.getCode()).forEach((x, y) -> {
            if (y.size() > 1) {
                sb.append("\n" + x + " 重复数：" + y.size());
            }
        });
        String error = sb.toString();
        if (!StringHelper.isNullOrWhitespace(error)) {
            throw new KnownException("代码中定义了重复的权限。详情如下：" + error);
        }
    }

    private static JList<Permission> getPermissions() throws Exception {
        StringBuilder errorSb = new StringBuilder();
        JList<Permission> permissions = new JList<>();

        Map<String, PermissionDefinition> map = AppContext.getBeans(PermissionDefinition.class);
        for (String key : map.keySet()) {
            PermissionDefinition definition = map.get(key);
            Class innerClass = definition.getClass();
            JList<String> codes = new JList<>();
            for (Field field : innerClass.getFields()) {
                codes.add(field.get(null).toString());
            }

            JList<Permission> innerPermissions = new JList<>();
            definition.registerDefinitions(innerPermissions);
            for (String code : codes) {
                Permission innerPermission = innerPermissions.firstOrNull(x -> Objects.equals(x.getCode(), code));
                if (innerPermission == null) {
                    String error = "\n" + innerClass.getName() + " 权限配置缺失：" + code + " 未注册到字典";
                    errorSb.append(error);
                    permissions.add(new Permission("未分组", "未分组", null, code));
                } else {
                    permissions.add(innerPermission);
                }
            }
            JList<Permission> redundantPermissions = innerPermissions.where(i -> !codes.any(c -> Objects.equals(i.getCode(), c)));
            if (redundantPermissions.size() > 0) {
                String error = "\n" + innerClass.getName() + " 权限配置多余，数量：" + permissions.size();
                errorSb.append(error);
            }
        }

        String error = errorSb.toString();
        if (!StringHelper.isNullOrWhitespace(error)) {
            LogHelper.log("代码错误-权限代码与注册信息不一致", error);
        }
        return permissions;
    }

}
