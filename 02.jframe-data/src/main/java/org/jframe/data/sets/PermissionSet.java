package org.jframe.data.sets;

import org.jframe.core.extensions.JList;
import org.jframe.core.hibernate.DbContext;
import org.jframe.core.hibernate.DbSet;
import org.jframe.data.entities.Permission;

/**
 * Created by Leo on 2018/1/12.
 */
public class PermissionSet extends DbSet<Permission> {

    public PermissionSet(DbContext db) {
        super(db, Permission.class);
    }

    public Integer getDictCount() {
        return super.count("where name is null or `group` is null");
    }

    public JList<Permission> getPermissionsByCodes(JList<String> codes) {
        if (codes == null || codes.size() == 0) {
            return new JList<>();
        }
        return super.getList("where code in :p0 ", codes);
    }


}
