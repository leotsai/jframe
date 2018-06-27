package org.jframe.data.sets;

import org.jframe.core.extensions.JList;
import org.jframe.core.hibernate.DbContext;
import org.jframe.core.hibernate.DbSet;
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

}
