package org.jframe.service.demo.dto;

import org.jframe.core.extensions.JList;
import org.jframe.core.web.TreeItem;
import org.jframe.data.entities.Role;

/**
 * @author luoh
 * @date 2018/1/19 10:16
 */
public class RolePermissionDto {
    private Long id;
    private String name;
    private String description;
    private JList<TreeItem> allPermissions;

    public RolePermissionDto(){

    }

    public RolePermissionDto(Role role, JList<TreeItem> permissions){
        this.id = role.getId();
        this.name = role .getName();
        this.description = role.getDescription();
        this.allPermissions = permissions;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public JList<TreeItem> getAllPermissions() {
        return allPermissions;
    }
}
