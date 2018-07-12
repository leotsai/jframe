package org.jframe.data.caching;

import org.jframe.core.extensions.JList;
import org.jframe.core.extensions.JMap;
import org.jframe.data.JframeDbContext;
import org.jframe.data.entities.Setting;
import org.jframe.data.enums.DbCacheKey;
import org.jframe.data.enums.SettingType;

import java.util.Objects;

/**
 * Created by leo on 2017-08-08.
 */
public class SettingContext implements VersionedCacheContext {

    private static final SettingContext instance = new SettingContext();

    public static SettingContext getInstance() {
        return instance;
    }

    private SettingContext() {

    }

    private long version;

    private boolean enableSms;


    @Override
    public void initialize(long version) {
        JList<Setting> list = new JList<>();
        list.add(new Setting("app.sms.enable", "true", "短信开关", SettingType.BOOLEAN, null));

        list.add(new Setting("app.test.text", "112233", "TEXT测试", SettingType.TEXT, null));
        list.add(new Setting("app.test.json", "{\"key\":\"value\"}", "JSON测试", SettingType.JSON, null));
        list.add(new Setting("app.test.checkbox", "[\"v1\",\"v2\",\"v3\"]", "CHECKBOX测试", SettingType.CHECKBOX, "v1,v2,v3,v4,v5"));
        list.add(new Setting("app.test.select", "v1", "SELECT测试", SettingType.SELECT, "v1,v2,v3,v4,v5"));

        this.init(list);
        this.refresh(version);
    }

    @Override
    public DbCacheKey getCacheKey() {
        return DbCacheKey.SETTING;
    }

    @Override
    public void refresh(long version) {
        this.version = version;

        JMap<String, String> map = this.getAll().toMap(x -> x.getKey(), x -> x.getValue());
        this.enableSms = map.getBoolean("app.sms.enable");
    }

    private void init(JList<Setting> entities) {
        int deleted = 0;
        int updated = 0;
        int added = 0;
        try (JframeDbContext db = new JframeDbContext()) {
            JList<Setting> dbEntities = db.getSettingSet().getAll();
            for (Setting dbItem : dbEntities) {
                Setting newItem = entities.firstOrNull(x -> Objects.equals(dbItem.getKey(), x.getKey()));
                if (newItem == null) {
                    db.delete(dbItem);
                    deleted++;
                } else {
                    if (!Objects.equals(dbItem.getNote(), newItem.getNote())) {
                        dbItem.setNote(newItem.getNote());
                        db.save(dbItem);
                        updated++;
                    }
                }
            }
            for (Setting newItem : entities) {
                Setting dbItem = dbEntities.firstOrNull(x -> Objects.equals(newItem.getKey(), x.getKey()));
                if (dbItem == null) {
                    db.save(newItem);
                    added++;
                }
            }
            db.commitTransaction();
        }
        System.out.println("初始化settings完成，新增：" + added + "，更新备注：" + updated + "，删除：" + deleted);
    }

    private JList<Setting> getAll() {
        try (JframeDbContext db = new JframeDbContext()) {
            return db.getSettingSet().getAll();
        }
    }

    @Override
    public long getVersion() {
        return version;
    }

    public boolean isEnableSms() {
        return enableSms;
    }
}
