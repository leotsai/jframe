package org.jframe.web.controllers;

import org.jframe.core.captcha.CaptchaBuilder;
import org.jframe.core.exception.FuckException;
import org.jframe.core.exception.KnownException;
import org.jframe.core.helpers.StringHelper;
import org.jframe.core.logging.LogHelper;
import org.jframe.core.web.RestPost;
import org.jframe.core.web.StandardJsonResult;
import org.jframe.infrastructure.AppContext;
import org.jframe.infrastructure.sms.CaptchaUsage;
import org.jframe.services.RedisApi;
import org.jframe.services.UserService;
import org.jframe.services.utils.SmsUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * Created by leo on 2017-09-17.
 */
@Controller
@RequestMapping("/captcha")
public class CaptchaController extends _ControllerBase {

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public void refresh(HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);

            String code = StringHelper.newImageCaptchaCode();
            BufferedImage image = CaptchaBuilder.build(code);
            ImageIO.write(image, "png", response.getOutputStream());
            RedisApi.setCurrentCaptcha(code);
            response.getOutputStream().close();
        } catch (Exception ex) {
            LogHelper.log("captcha.refresh", ex);
        }
    }

    @RestPost("/send")
    public StandardJsonResult send(String phone, CaptchaUsage usage, String captcha) {
        return super.tryJson(() -> {
            if (StringHelper.isNullOrWhitespace(phone) || !StringHelper.isPhoneNumber(phone)) {
                throw new KnownException("请输入正确的手机号");
            }
            StringHelper.validate_notNullOrWhitespace(captcha, "请输入图形验证码");
            if (!StringHelper.eq(RedisApi.getCurrentCaptcha(), captcha)) {
                throw new KnownException("验证码输入错误");
            }
            UserService userService = AppContext.getBean(UserService.class);
            String code = StringHelper.newPhoneCaptchaCode();
            switch (usage) {
                case REGISTER:
                    break;
                case RESET_PASSWORD:
                    if (userService.get(phone) == null) {
                        throw new KnownException("该手机号还没有被注册");
                    }
                    break;
                case SMS_LOGIN:
                    break;
                default:
                    throw new KnownException("参数错误");
            }
            SmsUtil.send(phone, code, usage);
            RedisApi.setCurrentSmsPhone(phone);
        });
    }

    @RestPost("/resend")
    public StandardJsonResult resend(String phone, CaptchaUsage usage) {
        return super.tryJson(() -> {
            if (StringHelper.isNullOrWhitespace(phone) || !StringHelper.isPhoneNumber(phone)) {
                throw new KnownException("请输入正确的手机号");
            }
            String code = StringHelper.newPhoneCaptchaCode();
            String currentSmsPhone = RedisApi.getCurrentSmsPhone();
            if (!Objects.equals(currentSmsPhone, phone)) {
                throw new FuckException();
            }
            if (usage == null || usage == CaptchaUsage.GENERAL) {
                throw new KnownException("参数错误");
            }
            SmsUtil.send(phone, code, usage);
        });
    }

}
