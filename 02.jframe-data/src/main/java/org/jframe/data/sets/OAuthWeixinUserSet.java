package org.jframe.data.sets;

import org.jframe.core.extensions.JList;
import org.jframe.core.hibernate.DbContext;
import org.jframe.core.hibernate.DbSet;
import org.jframe.data.entities.OAuthWeixinUser;

/**
 * @author:qq
 * @date:2017/10/16
 */
public class OAuthWeixinUserSet extends DbSet<OAuthWeixinUser> {
    public OAuthWeixinUserSet(DbContext db) {
        super(db, OAuthWeixinUser.class);
    }

    public OAuthWeixinUser getByOpenId(String openId) {
        return super.getFirst("where open_id=:p0", openId);
    }

    public OAuthWeixinUser getByUserId(Long userId) {
        return super.getFirst("where user_id=:p0", userId);
    }

    public JList<OAuthWeixinUser> getByUserIds(JList<Long> userIds) {
        return super.getList("where user_id in :p0", userIds);
    }

    public boolean canBind(Long userId, String openId) {
        OAuthWeixinUser weixinUser = this.getByOpenId(openId);
        if (weixinUser == null) {
            return false;
        }
        if (weixinUser.getUserId() != null) {
            return false;
        }
        return null == this.getByUserId(userId);
    }

    public JList<OAuthWeixinUser> getSubscribedWeixinUser() {
        return super.getList("where is_subscribed = TRUE");
    }

}