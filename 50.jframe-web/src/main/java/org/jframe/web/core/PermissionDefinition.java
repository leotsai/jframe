package org.jframe.web.core;

import org.jframe.core.extensions.JList;
import org.jframe.data.entities.Permission;

/**
 * Created by Leo on 2018/1/11.
 */
public interface PermissionDefinition {

    void registerDefinitions(JList<Permission> permissions);

    void registerMenuPermissions();

}
