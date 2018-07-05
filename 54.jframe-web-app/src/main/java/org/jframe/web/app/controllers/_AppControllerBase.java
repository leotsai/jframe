package org.jframe.web.app.controllers;

import org.jframe.web.controllers._ControllerBase;
import org.jframe.web.security.Authorize;
import org.jframe.web.security.WeixinAutoLogin;
import org.jframe.web.viewModels.MessageViewModel;
import org.springframework.web.servlet.ModelAndView;

/**
 * Author:Lsep
 * Date:2017/9/12
 */
@WeixinAutoLogin
@Authorize
public class _AppControllerBase extends _ControllerBase {

    @Override
    protected String getErrorViewPath() {
        return "app-message";
    }

    @Override
    protected ModelAndView error(String message) {
        MessageViewModel model = new MessageViewModel("出错啦");
        model.setError(message);
        model.setIcon("error");
        model.setError(message);
        return this.message(model);
    }

    protected ModelAndView message(String title, String message) {
        MessageViewModel model = new MessageViewModel(title);
        model.setIcon("info");
        model.setError(message);
        return this.message(model);
    }

    protected ModelAndView message(MessageViewModel model) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName(this.getErrorViewPath());
        mv.addObject("model", model);
        return mv;
    }


}
