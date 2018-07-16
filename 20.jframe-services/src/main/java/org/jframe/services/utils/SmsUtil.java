package org.jframe.services.utils;

import org.jframe.core.aliyun.sms.SmsTemplate;
import org.jframe.core.exception.KnownException;
import org.jframe.core.exception.SmsKnownException;
import org.jframe.core.extensions.JList;
import org.jframe.core.helpers.HttpHelper;
import org.jframe.core.helpers.JsonHelper;
import org.jframe.core.helpers.StringHelper;
import org.jframe.data.caching.SettingContext;
import org.jframe.infrastructure.AppContext;
import org.jframe.infrastructure.redis.JframeRedisSession;
import org.jframe.infrastructure.sms.CaptchaUsage;
import org.jframe.infrastructure.sms.JframeSmsApi;
import org.jframe.infrastructure.sms.templates.*;
import org.jframe.services.dto.SmsCaptcha;

import java.util.Objects;

/**
 * 短信发送工具
 *
 * @author qq
 * @date 2018/7/10
 */
public class SmsUtil {
    /**
     * 发送间隔（秒）
     */
    private static final int INTERVAL_SECONDS = 60;
    /**
     * 有效期（秒）
     */
    private static final int EXPIRE_SECONDS = 60 * 15;
    /**
     * 验证错误次数限制
     */
    private static final int ERROR_LIMIT = 5;
    /**
     * 发送次数重置周期（秒）
     */
    private static final int SEND_CYCLE_SECONDS = 12 * 60 * 60;

    public static void send(String phone, String code, CaptchaUsage usage) {
        SmsCaptchaTemplate template = getSmsTemplate(phone, code, usage);
        send(template);
    }

    public static void send(SmsTemplate template) {
        beforeSend(template);
        JframeSmsApi.getInstance().trySend(template);
        afterSend(template);
    }

    public static void validate(String phone, String code, CaptchaUsage usage) {
        validate(phone, code, usage, false);
    }

    public static void validate(String phone, String code, CaptchaUsage usage, boolean canUseAgain) {
        try (JframeRedisSession session = new JframeRedisSession()) {
            SmsTemplate template = getSmsTemplate(phone, code, usage);
            String captchaKey = AppContext.RedisKeys.smsCaptcha(parseTemplateCodeAndPhoneToKey(template.getTemplateCode(), phone));
            if (session.exists(captchaKey)) {
                SmsCaptcha smsCaptcha = JsonHelper.deserialize(session.get(captchaKey), SmsCaptcha.class);
                if (smsCaptcha == null) {
                    throw new SmsKnownException("请重新发送验证码");
                }
                if (smsCaptcha.getTries() >= ERROR_LIMIT) {
                    throw new SmsKnownException("输入错误次数太多，请重新获取验证码！");
                }
                boolean legal = Objects.equals(code, smsCaptcha.getCode());
                if (legal) {
                    deleteSession(session, phone, template.getTemplateCode(), canUseAgain);
                } else {
                    smsCaptcha.tried();
                    session.setex(captchaKey, EXPIRE_SECONDS, JsonHelper.serialize(smsCaptcha));
                    throw new SmsKnownException("验证码错误！");
                }
            } else {
                throw new SmsKnownException("验证码不合法！");
            }
        }
    }

    private static void beforeSend(SmsTemplate template) {
        if (!SettingContext.getInstance().isEnableSms()) {
            throw new SmsKnownException("短信功能已暂停使用！");
        }
        try (JframeRedisSession session = new JframeRedisSession()) {
            JList<String> phones = template.getPhoneNumbers();
            phones.forEach(x -> {
                validateSendInterval(session, x, template.getTemplateCode());
                validateSendIp(session);
                if (template instanceof SmsCaptchaTemplate) {
                    validateSendTimes(session, ((SmsCaptchaTemplate) template).getUsage(), x, template.getTemplateCode());
                }
            });
        }
    }

    private static void afterSend(SmsTemplate template) {
        try (JframeRedisSession session = new JframeRedisSession()) {
            JList<String> phones = template.getPhoneNumbers();
            phones.forEach(x -> {
                if (template instanceof SmsCaptchaTemplate) {
                    SmsCaptchaTemplate smsCaptchaTemplate = (SmsCaptchaTemplate) template;
                    addCaptcha(session, x, smsCaptchaTemplate.getCode(), smsCaptchaTemplate.getUsage());
                }
                updateInterval(session, x, template.getTemplateCode());
                updateIp(session);
                updateSendTimes(session, x, template.getTemplateCode());
            });
        }
    }

    private static void deleteSession(JframeRedisSession session, String phone, String templateCode, boolean canUseAgain) {
        String keySeed = parseTemplateCodeAndPhoneToKey(templateCode, phone);
        session.del(AppContext.RedisKeys.smsInterval(keySeed));
        if (!canUseAgain) {
            session.del(AppContext.RedisKeys.smsCaptcha(keySeed));
        }
    }

    private static void addCaptcha(JframeRedisSession session, String phone, String code, CaptchaUsage usage) {
        String keySeed = parseTemplateCodeAndPhoneToKey(getSmsTemplate(phone, code, usage).getTemplateCode(), phone);
        session.setex(AppContext.RedisKeys.smsCaptcha(keySeed), EXPIRE_SECONDS, JsonHelper.serialize(new SmsCaptcha(phone, code, usage)));
    }

    private static void updateInterval(JframeRedisSession session, String phone, String templateCode) {
        String keySeed = parseTemplateCodeAndPhoneToKey(templateCode, phone);
        session.setex(AppContext.RedisKeys.smsInterval(keySeed), INTERVAL_SECONDS, "true");
    }

    private static void updateIp(JframeRedisSession session) {
        String ip = HttpHelper.getIp();
        if (ip != null && !Objects.equals(ip, "0.0.0.0") && !Objects.equals(ip, "127.0.0.1")) {
            if (session.exists(AppContext.RedisKeys.smsIpLimit(ip))) {
                session.incr(AppContext.RedisKeys.smsIpLimit(ip));
            } else {
                session.setex(AppContext.RedisKeys.smsIpLimit(ip), SEND_CYCLE_SECONDS, "1");
            }
        }
    }

    private static void updateSendTimes(JframeRedisSession session, String phone, String templateCode) {
        String keySeed = parseTemplateCodeAndPhoneToKey(templateCode, phone);
        if (session.exists(AppContext.RedisKeys.smsTimesLimit(keySeed))) {
            session.incr(AppContext.RedisKeys.smsTimesLimit(keySeed));
        } else {
            session.setex(AppContext.RedisKeys.smsTimesLimit(keySeed), SEND_CYCLE_SECONDS, "1");
        }
    }

    private static void validateSendTimes(JframeRedisSession session, CaptchaUsage usage, String phone, String templateCode) {
        String currentTimes = session.get(AppContext.RedisKeys.smsTimesLimit(parseTemplateCodeAndPhoneToKey(templateCode, phone)));
        if (StringHelper.isNullOrEmpty(currentTimes)) {
            return;
        }
        if (Integer.valueOf(currentTimes) >= getTimesLimitedSetting(usage)) {
            throw new SmsKnownException("短信发送次数超过限制！");
        }

    }

    private static void validateSendInterval(JframeRedisSession session, String phone, String templateCode) {
        if (session.exists(AppContext.RedisKeys.smsInterval(parseTemplateCodeAndPhoneToKey(templateCode, phone)))) {
            throw new SmsKnownException("短信发送太快了，请稍后再试！");
        }
    }

    private static void validateSendIp(JframeRedisSession session) {
        String ip = HttpHelper.getIp();
        if (ip == null || Objects.equals(ip, "0.0.0.0") || Objects.equals(ip, "127.0.0.1")) {
            return;
        }
        String count = session.get(AppContext.RedisKeys.smsIpLimit(ip.replace(".", "_")));
        if (count == null) {
            return;
        }
        if (Integer.valueOf(count) > getIpLimitedSetting()) {
            throw new KnownException("当前IP短信发送超出限制");
        }
    }

    private static SmsCaptchaTemplate getSmsTemplate(String phone, String code, CaptchaUsage usage) {
        switch (usage) {
            case REGISTER:
                return new RegisterSmsCaptchaTemplate(phone, code);
            case SMS_LOGIN:
                return new LoginSmsCaptchaTemplate(phone, code);
            case RESET_PASSWORD:
                return new FindPasswordSmsCaptchaTemplate(phone, code);
            case RESET_PAY_PASSWORD:
                return new ResetPayPasswordSmsCaptchaTemplate(phone, code);
            default:
                return new GeneralCaptchaSmsCaptchaTemplate(phone, code);
        }
    }

    private static int getIpLimitedSetting() {
        return AppContext.getAppConfig().getSmsIpLimit();
    }

    private static int getTimesLimitedSetting(CaptchaUsage usage) {
        switch (usage) {
            case REGISTER:
                return AppContext.getAppConfig().getSmsRegisterLimit();
            case SMS_LOGIN:
                return AppContext.getAppConfig().getSmsLoginLimit();
            case RESET_PASSWORD:
                return AppContext.getAppConfig().getSmsResetPasswordLimit();
            case RESET_PAY_PASSWORD:
                return AppContext.getAppConfig().getSmsResetPayPasswordLimit();
            default:
                return AppContext.getAppConfig().getSmsGeneralLimit();
        }
    }

    private static String parseTemplateCodeAndPhoneToKey(String templateCode, String phone) {
        return templateCode + phone;
    }
}
