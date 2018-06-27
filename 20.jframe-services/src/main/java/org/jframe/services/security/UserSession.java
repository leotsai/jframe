package org.jframe.services.security;

import org.jframe.core.extensions.JList;
import org.jframe.core.extensions.KnownException;
import org.jframe.data.caching.RoleContext;
import org.jframe.data.entities.Role;
import org.jframe.data.entities.User;
import org.jframe.data.enums.VisualRole;

import java.util.Objects;

/**
 * Created by leo on 2017-08-22.
 */
public class UserSession {
    private Long id;
    private String username;
    private String nickname;
    private String ip;
    private JList<String> roles;

    public UserSession() {
        this.roles = new JList<>();
    }

    public UserSession(User user, String ip, JList<String> roles) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.username = user.getUsername();
        this.ip = ip;
        this.roles = roles;
    }

    public JList<String> getAllPermissions() {
        JList<String> permissions = new JList<>();
        if (this.roles == null) {
            return permissions;
        }
        for (String roleName : this.roles) {
            permissions.addAll(RoleContext.getInstance().getPermissions(roleName));
        }
        return permissions;
    }

    public boolean hasPermission(String code) {
        if (this.isSuperAdmin()) {
            return true;
        }
        return this.getAllPermissions().any(p -> p.startsWith(code));
    }

    public boolean isAdmin() {
        return this.roles != null && this.roles.size() > 0;
    }

    public boolean isSuperAdmin() {
        return this.roles.any(r -> Objects.equals(r, Role.Names.SUPER_ADMIN));
    }

    //==========================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public JList<String> getRoles() {
        return roles;
    }

    public void setRoles(JList<String> roles) {
        this.roles = roles;
    }

}
