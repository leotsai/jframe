package org.jframe.data.entities;

import org.jframe.data.core.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * Created by leo on 2017-05-25.
 * 用户-角色关系表
 */
@Entity
@Table(name = "s_user_role_rls", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_role_id", columnList = "role_id")
})
public class UserRoleRL extends EntityBase {
    @Column(name = "user_id", columnDefinition = "bigint not null COMMENT '对应用户表的id列'")
    private Long userId;

    @Column(name = "role_id", columnDefinition = "bigint not null COMMENT '对应角色表的id列'")
    private Long roleId;

    public UserRoleRL() {

    }

    public UserRoleRL(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
    //-------------------------------------

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

}
