package org.jframe.web.pc.controllers;


import org.jframe.web.controllers._ControllerBase;

/**
 * Created by Leo on 2017/1/7.
 */
public class _PcControllerBase extends _ControllerBase {
    @Override
    protected String getAreaPathPrefix() {
        return "/modules/pc/views/";
    }

    @Override
    protected String getErrorViewPath(){
        return "/modules/pc/views/_shared/error.jsp";
    }
}
