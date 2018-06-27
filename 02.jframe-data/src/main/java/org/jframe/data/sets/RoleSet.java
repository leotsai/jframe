package org.jframe.data.sets;

import org.jframe.core.extensions.JList;
import org.jframe.core.hibernate.DbContext;
import org.jframe.core.hibernate.DbSet;
import org.jframe.data.entities.Role;


/**
 * @author Lsep
 * @date 2017/9/7
 */
public class RoleSet extends DbSet<Role> {

    public RoleSet(DbContext db) {
        super(db, Role.class);
    }

    public Role get(String name) {
        return super.getFirst("where name =:p0", name);
    }

    public JList<String> getUserRoles(Long userId) {
        String sql = "select r.name from s_user_role_rls as rl inner join s_roles as r on rl.role_id = r.id " +
                "where rl.user_id=:p0";
        return super.db.getList(sql, userId);
    }

}
