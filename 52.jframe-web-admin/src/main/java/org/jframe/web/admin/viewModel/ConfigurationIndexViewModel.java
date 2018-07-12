package org.jframe.web.admin.viewModel;

import org.jframe.core.extensions.JList;
import org.jframe.infrastructure.AppContext;
import org.jframe.service.admin.ConfigurationService;
import org.jframe.service.admin.dto.ConfigurationDto;
import org.jframe.web.admin.Menu;
import org.jframe.web.admin.score.AdminLayoutViewModel;

/**
 * @author qq
 * @date 2018/7/11
 */
public class ConfigurationIndexViewModel extends AdminLayoutViewModel {
    private JList<ConfigurationDto> configs;

    public ConfigurationIndexViewModel() {
        super("配置");
        super.setCurrentPage(Menu.system().setting().config());
    }

    public ConfigurationIndexViewModel build() {
        this.configs = AppContext.getBean(ConfigurationService.class).getAll().select(ConfigurationDto::new);
        return this;
    }

    public JList<ConfigurationDto> getConfigs() {
        return configs;
    }

    public void setConfigs(JList<ConfigurationDto> configs) {
        this.configs = configs;
    }
}
