package org.jframe.web.controllers;

import org.apache.commons.io.FileUtils;
import org.jframe.core.extensions.Action1;
import org.jframe.core.extensions.KnownException;
import org.jframe.core.extensions.ThrowableAction;
import org.jframe.core.extensions.ThrowableFunction0;
import org.jframe.core.helpers.ExceptionHelper;
import org.jframe.core.helpers.HttpHelper;
import org.jframe.core.helpers.RequestHelper;
import org.jframe.core.logging.LogHelper;
import org.jframe.core.web.StandardJsonResult;
import org.jframe.infrastructure.AppContext;
import org.jframe.web.security.WebContext;
import org.jframe.web.viewModels.LayoutViewModel;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by leo on 2016-12-16.
 */
public class _ControllerBase {

    protected String getErrorViewPath() {
        return "/modules/_public/views/error.jsp";
    }

    protected String getAreaPathPrefix() {
        return null;
    }

    protected <T> StandardJsonResult<T> tryJson(ThrowableFunction0<T> func) {
        StandardJsonResult result = new StandardJsonResult();
        try {
            result.setValue(func.apply());
            result.setSuccess(true);
        } catch (KnownException ex) {
            result.fail(ex.getMessage(), ex.getCode());
        } catch (Throwable ex) {
            result.fail(this.getUserMessage(ex), "-1");
            LogHelper.log("_ControllerBase.tryJson", ex);
        }
        return result;
    }

    protected StandardJsonResult tryJson(ThrowableAction action) {
        StandardJsonResult result = new StandardJsonResult();
        try {
            action.apply();
            result.setSuccess(true);
        } catch (KnownException ex) {
            result.fail(ex.getMessage(), ex.getCode());
        } catch (Throwable ex) {
            result.fail(this.getUserMessage(ex), "-1");
            LogHelper.log("_ControllerBase.tryJson", ex);
        }
        return result;
    }

    protected ModelAndView tryView(String path, ThrowableFunction0<Object> getModel) {
        return this.tryView(path, getModel, null);
    }

    protected ModelAndView tryView(String path, ThrowableFunction0<Object> getModel, Action1<ModelAndView> mvBuilder) {
        ModelAndView mv = new ModelAndView();
        String prefix = this.getAreaPathPrefix();
        String viewName = path.endsWith(".jsp") ?  (prefix == null ? path : prefix + path) : path;
        try {
            mv.setViewName(viewName);
            if (getModel != null) {
                mv.addObject("model", getModel.apply());
            }
            if (mvBuilder != null) {
                mvBuilder.apply(mv);
            }
            return mv;
        } catch (Throwable ex) {
            if (ex instanceof KnownException) {
                if (RequestHelper.isAjax()) {
                    return this.writeToResponse(ex.getMessage());
                }
                return this.error(ex.getMessage());
            }
            LogHelper.log("_ControllerBase.tryView", ex);
            if (RequestHelper.isAjax()) {
                return this.writeToResponse(this.getUserMessage(ex));
            }
            return this.error(this.getUserMessage(ex));
        }
    }


    private ModelAndView writeToResponse(String message) {
        try {
            HttpServletResponse response = HttpHelper.getCurrentResponse();
            response.setStatus(210);
            response.setHeader("Content-Type", "application/json;charset=utf-8");
            response.getWriter().write(message);
        } catch (IOException e) {
            LogHelper.log("response.write", e);
        }
        return null;
    }

    private String getUserMessage(Throwable ex) {
        return AppContext.getAppConfig().isTestServer() ? ExceptionHelper.getFullHtmlMessage(ex) : "服务器未知错误，请刷新页面重试";
    }

    protected ModelAndView error(String message) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName(this.getErrorViewPath());

        LayoutViewModel model = new LayoutViewModel("出错啦");
        model.setError(message);

        mv.addObject("model", model);
        return mv;
    }

    protected Long getUserId() {
        return WebContext.getCurrent().getSession().getId();
    }

    protected void validate(boolean valueIsValid, String errorMessage) {
        if (!valueIsValid) {
            throw new KnownException(errorMessage);
        }
    }

    protected String readFileText(String relativePath) throws Exception {
        Path physicalPath = Paths.get(AppContext.getStartupDirectory(), relativePath);
        return FileUtils.readFileToString(physicalPath.toFile(), "UTF-8");
    }

}
