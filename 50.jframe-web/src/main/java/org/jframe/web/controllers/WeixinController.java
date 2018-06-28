package org.jframe.web.controllers;

import org.jframe.core.extensions.JDate;
import org.jframe.core.extensions.JList;
import org.jframe.core.extensions.KnownException;
import org.jframe.core.helpers.StringHelper;
import org.jframe.core.logging.LogHelper;
import org.jframe.core.web.RestPost;
import org.jframe.core.web.StandardJsonResult;
import org.jframe.core.weixin.core.WeixinJsConfigDto;
import org.jframe.core.weixin.core.dtos.OAuthOpenIdDto;
import org.jframe.data.entities.User;
import org.jframe.infrastructure.AppContext;
import org.jframe.infrastructure.helpers.CookieHelper;
import org.jframe.infrastructure.weixin.JframeWeixinApi;
import org.jframe.services.UserService;
import org.jframe.services.flow.CreateWeixinUserIfNecessaryFlow;
import org.jframe.services.security.UserSession;
import org.jframe.web.security.WebContext;
import org.jframe.web.security.WebIdentity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author qq
 * @date 2018/6/27
 */
@Controller("web-weixin")
@RequestMapping("/weixin")
public class WeixinController extends _ControllerBase {

    @GetMapping("/oauth")
    public ModelAndView OAuth(String code, String state, String returnUrl, HttpServletResponse response) throws
            Exception {
        if (StringHelper.isNullOrWhitespace(code)) {
            return this.oauthError(returnUrl);
        }
        OAuthOpenIdDto openIdDto = JframeWeixinApi.getInstance().getOpenIdByCode(code);
        if (openIdDto == null || !openIdDto.isSuccess()) {
            return this.oauthError(returnUrl);
        }
        String openId = openIdDto.getOpenid();
        CookieHelper.setWeixinOpenId(openId);
        this.setFromUserIdToCookie(returnUrl);
        CreateWeixinUserIfNecessaryFlow flow = new CreateWeixinUserIfNecessaryFlow();
        flow.run(openId, openIdDto.getAccess_token());
        return redirect(response, returnUrl);
    }

    @GetMapping("/silentOauth")
    public ModelAndView silentOauth(String code, String state, String returnUrl, HttpServletResponse response) throws
            Exception {
        if (StringHelper.isNullOrWhitespace(code)) {
            return this.oauthError(returnUrl);
        }
        OAuthOpenIdDto openIdDto = JframeWeixinApi.getInstance().getOpenIdByCode(code);
        if (openIdDto == null || !openIdDto.isSuccess()) {
            return this.oauthError(returnUrl);
        }
        String openId = openIdDto.getOpenid();
        CookieHelper.setWeixinOpenId(openId);
        return redirect(response, returnUrl);
    }

    @GetMapping("/autoLogin")
    public ModelAndView oauthLogin(String code, String state, String returnUrl, HttpServletResponse response) throws
            Exception {

        if (StringHelper.isNullOrWhitespace(code)) {
            return this.oauthError(returnUrl);
        }
        OAuthOpenIdDto openIdDto = JframeWeixinApi.getInstance().getOpenIdByCode(code);
        if (openIdDto == null || !openIdDto.isSuccess()) {
            return this.oauthError(returnUrl);
        }
        CookieHelper.setWeixinOpenId(openIdDto.getOpenid());
        this.setFromUserIdToCookie(returnUrl);

        UserService service = AppContext.getBean(UserService.class);
        User user = service.getUserByWeixinOpenId(openIdDto.getOpenid());
        if (user == null) {
            CreateWeixinUserIfNecessaryFlow flow = new CreateWeixinUserIfNecessaryFlow();
            flow.run(openIdDto.getOpenid(), openIdDto.getAccess_token());
            return redirect(response, returnUrl);
        }
        if (user.isDisabled()) {
            return super.error("您的账户已被禁止登陆");
        }
        if (!user.isLoggedIn()) {
            return redirect(response, returnUrl);
        }
        service.logLogin(user.getId());
        WebContext.getCurrent().login(new WebIdentity(user.getUsername(), ""));
        return redirect(response, returnUrl);
    }

    private void setFromUserIdToCookie(String returnUrl) {
        if (returnUrl == null) {
            return;
        }
        try {
            String paraStr = returnUrl.substring(returnUrl.lastIndexOf("?") + 1);
            String[] paras = paraStr.split("&");
            String parameter = JList.from(paras).firstOrNull(x -> x.startsWith("fu="));
            if (!StringHelper.isNullOrWhitespace(parameter)) {
                String userId = parameter.substring(3);
                if (!StringHelper.isNullOrWhitespace(userId)) {
                    String cookieValue = String.valueOf(userId) + "#" + String.valueOf(JDate.now().getTime());
                    CookieHelper.setFromUserId(cookieValue);
                }
            }
        } catch (Exception e) {
            LogHelper.log("获取自动登录连接中的参数", e);
        }
    }

    private ModelAndView oauthError(String returnUrl) {
        return super.error("对不起，微信授权失败了，请返回上一页重新试一下吧~");
    }

    private ModelAndView redirect(HttpServletResponse response, String returnUrl) throws Exception {
        if (StringHelper.isNullOrWhitespace(returnUrl)) {
            response.sendRedirect("/app");
        } else {
            response.sendRedirect(returnUrl);
        }
        return null;
    }

    @RestPost("/getConfigOptions")
    public StandardJsonResult getConfigOptions(HttpServletRequest request) {
        return super.tryJson(() -> {
            String referer = request.getHeader("Referer");
            if (StringHelper.isNullOrWhitespace(referer)) {
                throw new KnownException("错误的AJAX请求");
            }
            UserSession userSession = WebContext.getCurrent().getSession();
            Long userId = userSession == null ? null : userSession.getId();
            return new WeixinJsConfigDto(JframeWeixinApi.getInstance(), userId).buildSignature(referer);
        });
    }
}
