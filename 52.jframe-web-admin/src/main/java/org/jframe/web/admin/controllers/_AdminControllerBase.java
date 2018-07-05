package org.jframe.web.admin.controllers;

import org.jframe.data.entities.Role;
import org.jframe.web.controllers._ControllerBase;
import org.jframe.web.security.Authorize;

/**
 * @author qq
 * @date 2018/6/26
 */
@Authorize(rolesNames = Role.Names.ADMIN)
public class _AdminControllerBase extends _ControllerBase {
}
