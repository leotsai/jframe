package org.jframe.service.demo;

import org.jframe.core.extensions.JList;
import org.jframe.core.extensions.JMap;
import org.jframe.data.entities.Role;

/**
 * Author:Lsep
 * Date:2017/9/21
 */
public interface RoleService {
    JList<Role> getAll();

    Role getById(Long id);

    JList<Role> getAllByUserId(Long userId);

    JMap<Role, JMap<String, JList<String>>> getAllRolePermissions();

}
