package org.jframe.service.admin.impl;

import org.jframe.core.extensions.JList;
import org.jframe.data.caching.DbCacheContext;
import org.jframe.data.entities.Setting;
import org.jframe.data.enums.DbCacheKey;
import org.jframe.service.admin.ConfigurationService;
import org.jframe.services.core.ServiceBase;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author qq
 * @date 2018/7/11
 */
@Service("admin-config-service")
public class ConfigurationServiceImpl extends ServiceBase implements ConfigurationService {
    @Override
    public JList<Setting> getAll() {
        return super.getFromDb(db -> db.getSettingSet().getAll());
    }

    @Override
    public void save(JList<Setting> settings) {
        super.useTransaction(db -> {
            JList<String> keys = settings.select(Setting::getKey);
            JList<Setting> dbSettings = db.getSettingSet().getByKeys(keys);
            dbSettings.forEach(x -> {
                x.setValue(settings.firstOrNull(s -> Objects.equals(x.getKey(), s.getKey())).getValue());
                x.validateValue();
                db.save(x);
            });
        });
        DbCacheContext.getInstance().refreshDbVersion(DbCacheKey.SETTING);
    }
}
