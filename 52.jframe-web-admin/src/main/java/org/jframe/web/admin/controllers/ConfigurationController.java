package org.jframe.web.admin.controllers;

import org.jframe.core.extensions.JList;
import org.jframe.core.web.RestPost;
import org.jframe.core.web.StandardJsonResult;
import org.jframe.data.entities.Role;
import org.jframe.data.entities.Setting;
import org.jframe.service.admin.ConfigurationService;
import org.jframe.service.admin.dto.ConfigurationDto;
import org.jframe.web.admin.viewModel.ConfigurationIndexViewModel;
import org.jframe.web.security.Authorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author qq
 * @date 2018/6/26
 */
@Controller("admin-config-controller")
@RequestMapping("/admin/config")
@Authorize(rolesNames = Role.Names.SUPER_ADMIN)
public class ConfigurationController extends _AdminControllerBase {

    @Autowired
    ConfigurationService service;

    @RequestMapping
    public ModelAndView index() {
        return super.tryView("admin-config-index", () -> new ConfigurationIndexViewModel().build());
    }

    @RestPost("/save")
    public StandardJsonResult save(ConfigurationIndexViewModel model) {
        return super.tryJson(() -> {
            JList<ConfigurationDto> configurationDtos = model.getConfigs();
            if (configurationDtos == null || configurationDtos.size() == 0) {
                return;
            }
            JList<Setting> settings = configurationDtos.select(x -> {
                Setting setting = new Setting();
                setting.setKey(x.getKey());
                setting.setValue(x.getValue());
                return setting;
            });
            service.save(settings);
        });
    }

}
