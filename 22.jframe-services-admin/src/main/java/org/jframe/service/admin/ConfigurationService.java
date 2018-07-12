package org.jframe.service.admin;

import org.jframe.core.extensions.JList;
import org.jframe.data.entities.Setting;

/**
 * @author qq
 * @date 2018/7/11
 */
public interface ConfigurationService {
    JList<Setting> getAll();
    void save(JList<Setting> settings);
}
