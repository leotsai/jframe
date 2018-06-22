package org.jframe.infrastructure.configs;

import org.jframe.infrastructure.AppContext;

/**
 * Created by Leo on 2017/11/17.
 */
public class WeixinUrls {

    public static String myFans(){
        return AppContext.getAppConfig().getHost()+"/app/me/invite";
    }

}
