package org.jframe.data.sets;


import org.jframe.core.extensions.JList;
import org.jframe.core.hibernate.DbContext;
import org.jframe.core.hibernate.DbSet;
import org.jframe.data.entities.Captcha;
import org.jframe.infrastructure.sms.CaptchaUsage;

/**
 * Created by leo on 2017/5/19.
 */
public class CaptchaSet extends DbSet<Captcha> {

    public CaptchaSet(DbContext db) {
        super(db, Captcha.class);
    }

    public JList<Captcha> getAllActives(String phone, CaptchaUsage usage) {
        String where = "where phone=:p0 and `usage`=:p1 and is_used = 0 and expire_time > now() order by id desc";
        return super.getList(where, phone, usage.getValue());
    }

    public Captcha getLatestUsed(String phone, CaptchaUsage usage, String code) {
        String where = "where phone =:p0 and `usage`=:p1 and code=:p2 and is_used = TRUE order by id desc";
        return super.getFirst(where, phone, usage.getValue(), code);
    }

    public JList<Captcha> getAll(String phone, CaptchaUsage usage) {
        String where = "where phone=:p0 and `usage`=:p1 order by id desc";
        return super.getList(where, phone, usage.getValue());
    }

}
