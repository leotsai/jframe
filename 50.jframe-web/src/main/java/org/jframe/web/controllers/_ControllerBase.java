package org.jframe.web.controllers;

import org.jframe.infrastructure.core.Action;
import org.jframe.infrastructure.core.Func;
import org.jframe.infrastructure.core.KnownException;
import org.jframe.infrastructure.helpers.LogHelper;
import org.jframe.infrastructure.web.StandardJsonResult;
import org.jframe.web.viewModels.LayoutViewModel;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by leo on 2016-12-16.
 */
public class _ControllerBase {

    protected String getErrorViewPath(){
        return "/modules/_shared/error.jsp";
    }

    protected String getAreaPathPrefix(){
        return null;
    }

    protected <T> StandardJsonResult<T> tryJson(Func<T> func){
        StandardJsonResult result = new StandardJsonResult();
        try{
            result.setValue(func.invoke());
            result.setSuccess(true);
        }
        catch (KnownException ex){
            result.fail(ex.getMessage());
        }
        catch (Exception  ex){
            result.fail(ex.getMessage(), -1);
            LogHelper.log("_ControllerBase.tryJson", ex);
            ex.printStackTrace();
        }
        return result;
    }

    protected  StandardJsonResult tryJson(Action action){
        StandardJsonResult result = new StandardJsonResult();
        try{
            action.invoke();
            result.setSuccess(true);
        }
        catch (KnownException ex){
            result.fail(ex.getMessage());
        }
        catch (Exception  ex){
            result.fail(ex.getMessage(), -1);
            LogHelper.log("_ControllerBase.tryJson", ex);
        }
        return result;
    }

    protected ModelAndView view(String path, Object model){
        String viewName = path.endsWith(".jsp") ? this.getAreaPathPrefix() + path : path;
        return new ModelAndView(viewName, "model", model);
    }

    protected ModelAndView tryView(String path, Func<Object> getModel){
        ModelAndView mv = new ModelAndView();
        String viewName = path.endsWith(".jsp") ? this.getAreaPathPrefix() + path : path;
        try{
            mv.setViewName(viewName);
            mv.addObject("model", getModel.invoke());
        }
        catch (KnownException ex){
            return error( ex.getMessage());
        }
        catch (Exception ex){
            LogHelper.log("_ControllerBase.tryView", ex);
            return error("服务器未知错误，请刷新页面重试");
        }
        return mv;
    }

    protected  ModelAndView error(String message){
        ModelAndView mv = new ModelAndView();
        mv.setViewName(this.getErrorViewPath());

        LayoutViewModel model = new LayoutViewModel();
        model.setError(message);

        mv.addObject("model", model);
        return mv;
    }



}
