package org.jframe.core.weixin.core.parameters;

/**
 * Created by leo on 2017-09-24.
 */
public class ShortenUrlParameter {
    private String action;
    private String long_url;

    public ShortenUrlParameter(){

    }

    public ShortenUrlParameter(String action, String long_url){
        this.action = action;
        this.long_url = long_url;
    }

}
