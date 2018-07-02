package org.jframe.data.sets;

import org.jframe.core.extensions.JList;
import org.jframe.core.hibernate.DbContext;
import org.jframe.core.hibernate.DbSet;
import org.jframe.data.entities.Role;
import org.jframe.data.entities.UserRoleRL;

/**
 * Author:Lsep
 * Date:2017/9/7
 */
public class UserRoleRLSet extends DbSet<UserRoleRL> {

    public UserRoleRLSet(DbContext db) {
        super(db, UserRoleRL.class);
    }

    public UserRoleRL getByUserIdAndRoled(Long userId, Long roleId) {
        return super.getFirst("where user_id =:p0 and role_id =:p1", userId, roleId);
    }

    public JList<UserRoleRL> getAllByUserId(Long userId) {
        return super.getList(" where user_id=:p0 ", userId);
    }


    public JList<UserRoleRL> getByRoleId(Long roleId) {
        return super.getList("where role_id =:p0", roleId);
    }

    public Integer getSuperAdminCount() {
        String sql = "select count(1) from s_user_role_rls rl join s_roles ro on rl.role_id = ro.id  where ro.name =:p0";
        return db.count(sql, Role.Names.SUPER_ADMIN);
    }
}
