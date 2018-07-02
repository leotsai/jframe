package org.jframe.service.admin;

import org.jframe.data.entities.Role;
import org.jframe.service.admin.dto.RolePermissionDto;

/**
 * @author luoh
 * @date 2018/1/18 17:02
 */
public interface RoleSettingService {

//    void saveRole(Role role);

    void savePermission(Role role);

    RolePermissionDto getPermissions(Long id);
}
