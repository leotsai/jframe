package org.jframe.data.sets;

import org.jframe.core.extensions.JList;
import org.jframe.core.hibernate.DbContext;
import org.jframe.core.hibernate.DbSet;
import org.jframe.data.entities.Setting;


public class SettingSet extends DbSet<Setting> {
    public SettingSet(DbContext db) {
        super(db, Setting.class);
    }

    public Setting getByKey(String key) {
        return super.getFirst("where `key`=:p0", key);
    }

    public JList<Setting> getByKeys(JList<String> keys) {
        if (keys == null || keys.size() == 0) {
            return new JList<>();
        }
        return super.getList("where `key`in :p0", keys);
    }

}
