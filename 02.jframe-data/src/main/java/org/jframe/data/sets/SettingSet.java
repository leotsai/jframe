package org.jframe.data.sets;

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

}
