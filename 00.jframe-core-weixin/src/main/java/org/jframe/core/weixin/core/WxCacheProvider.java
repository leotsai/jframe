package org.jframe.core.weixin.core;

import org.jframe.core.weixin.core.dtos.AccessTokenDto;
import org.jframe.core.weixin.core.dtos.JsApiTicketDto;

/**
 * Created by leo on 2017-09-24.
 */
public interface WxCacheProvider {

    AccessTokenDto getAccessToken(String appId);

    void setAccessToken(String appId, AccessTokenDto tokenDto);

    JsApiTicketDto getJsApiTicket(String appId);

    void setJsApiTicket(String appId, JsApiTicketDto ticketDto);
}
