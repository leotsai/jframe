package org.jframe.data.entities;

import org.jframe.data.core.EntityBase;

import javax.persistence.*;

/**
 * Created by leo on 2017-05-25.
 */
@Entity(name = "user_role_rls")
public class UserRoleRL extends EntityBase {
    @Column(name = "user_id", columnDefinition = "varchar(32) not null")
    private String userId;

    @Column(name = "role_id", columnDefinition = "varchar(32) not null")
    private String roleId;

    //-------------------------------------

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_user_role_rls_roleId"))
    private Role role;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_user_role_rls_userId"))
    private User user;

    //-------------------------------------

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
