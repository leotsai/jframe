package org.jframe.service.demo.impl;

import org.jframe.core.extensions.JList;
import org.jframe.core.extensions.JMap;
import org.jframe.data.entities.Permission;
import org.jframe.data.entities.Role;
import org.jframe.service.demo.RoleService;
import org.jframe.services.core.ServiceBase;
import org.springframework.stereotype.Service;

/**
 * Author:Lsep
 * Date:2017/9/21
 */
@Service
public class RoleServiceImpl extends ServiceBase implements RoleService {
    @Override
    public JList<Role> getAll() {
        return super.getFromDb(db -> db.getRoleSet().getAll());
    }

    @Override
    public JList<Role> getAllByUserId(Long userId) {
        return super.getFromDb(db->db.getRoleSet().getAllByUserId(userId));
    }

    @Override
    public Role getById(Long id) {
        return super.getFromDb(db -> db.getRoleSet().get(id));
    }

    @Override
    public JMap<Role, JMap<String, JList<String>>> getAllRolePermissions() {
        return super.getFromDb(db -> {
            JList<Role> roles = db.getRoleSet().getAllRoleIsNotSystem();
            JMap<Role, JMap<String, JList<String>>> allRolePermissions = new JMap<>();
            for (Role role : roles) {
                if (null == role.getCodes() || role.getCodes().size() == 0) {
                    continue;
                }
                JList<Permission> permissions = db.getPermissionSet().getPermissionsByCodes(role.getCodes());
                JMap<String, JList<String>> permissionGroup = new JMap<>();
                for (Permission permission : permissions) {
                    if (permissionGroup.containsKey(permission.getGroup())) {
                        JList<String> permissionNames = permissionGroup.get(permission.getGroup());
                        permissionNames.add(permission.getName());
                        permissionGroup.put(permission.getGroup(), permissionNames);
                        continue;
                    } else {
                        JList<String> permissionNames = new JList<>();
                        permissionNames.add(permission.getName());
                        permissionGroup.put(permission.getGroup(), permissionNames);
                    }
                }
                allRolePermissions.put(role, permissionGroup);
            }
            return allRolePermissions;
        });
    }
}
