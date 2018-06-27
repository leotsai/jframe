package org.jframe.services.impl;


import org.jframe.core.extensions.JDate;
import org.jframe.core.extensions.JList;
import org.jframe.core.extensions.KnownException;
import org.jframe.core.helpers.HttpHelper;
import org.jframe.data.JframeDbContext;
import org.jframe.data.entities.Captcha;
import org.jframe.data.enums.CaptchaUsage;
import org.jframe.infrastructure.helpers.DateHelper;
import org.jframe.services.CaptchaService;
import org.jframe.services.core.ServiceBase;
import org.springframework.stereotype.Service;

/**
 * Created by leo on 2017/5/16.
 */
@Service("service-captcha")
public class CaptchaServiceImpl extends ServiceBase implements CaptchaService {

    @Override
    public void add(String phone, String code, CaptchaUsage usage) {
        Captcha captcha = new Captcha();
        captcha.setCode(code);
        captcha.setPhone(phone);
        captcha.setUsage(usage);
        captcha.setFromIp(HttpHelper.getIp());
        captcha.setExpireTime(new JDate().addMinuts(2));
        super.useTransaction(db -> db.save(captcha));
    }

    @Override
    public void validateSmsCaptcha(String phone, String code, CaptchaUsage usage) {
        if (!isValid(phone, code, usage)) {
            throw new KnownException("短信验证码错误");
        }
    }

    @Override
    public boolean isValid(String phone, String code, CaptchaUsage usage) {
        try (JframeDbContext db = new JframeDbContext()) {
            JList<Captcha> captchas = db.getCaptchaSet().getAll(phone, usage);
            if (captchas.size() == 0) {
                return false;
            }
            JList<Captcha> correctCaptchas = captchas.where(x -> x.getCode().equalsIgnoreCase(code));
            if (correctCaptchas.size() > 0) {
                JList<Captcha> unUsedCaptchas = correctCaptchas.where(x -> !x.isUsed());
                if (unUsedCaptchas.size() == 0) {
                    throw new KnownException("验证码已使用，请重新获取验证码");
                }
                JList<Captcha> activeCaptchas = unUsedCaptchas.where(x -> DateHelper.compare(JDate.now(), JDate.from(x.getExpireTime())) != 1);
                if (activeCaptchas.size() == 0) {
                    throw new KnownException("验证码已过期，请重新获取验证码");
                }
                Captcha captcha = activeCaptchas.firstOrNull();
                if (captcha.getTries() > 5) {
                    throw new KnownException("您输入的验证码次数太多，请重新获取验证码");
                }
                captcha.setUsed(true);
                captcha.setTries(captcha.getTries() + 1);
                db.save(captcha);
                db.commitTransaction();
                return true;
            } else {
                Captcha captcha = captchas.firstOrNull(x -> !x.isUsed() && DateHelper.compare(JDate.now(), JDate.from(x.getExpireTime())) != 1);
                if (captcha != null) {
                    captcha.setTries(captcha.getTries() + 1);
                    db.save(captcha);
                    db.commitTransaction();
                }
                return false;
            }
        }
    }

    @Override
    public void markLatestCaptchaUnused(String phone, CaptchaUsage usage, String code) {
        try (JframeDbContext db = new JframeDbContext()) {
            Captcha captcha = db.getCaptchaSet().getLatestUsed(phone, usage, code);
            if (captcha != null) {
                captcha.setUsed(false);
                captcha.setTries(captcha.getTries() - 1);
                db.save(captcha);
                db.commitTransaction();
            }
        }
    }

}
