package org.jframe.data;

import org.jframe.core.hibernate.DbContext;
import org.jframe.data.sets.*;

/**
 * Created by leo on 2017-05-31.
 */
public class JframeDbContext extends DbContext {
    private UserSet userSet;
    private RoleSet roleSet;
    private UserRoleRLSet userRoleRLSet;
    private CaptchaSet captchaSet;
    private OAuthWeixinUserSet oAuthWeixinUserSet;

    public JframeDbContext() {
        super(JframeHibernateSessionFactory.getInstance());
    }

    public JframeDbContext(boolean transactional) {
        super(JframeHibernateSessionFactory.getInstance(), transactional);
    }

    public UserSet getUserSet() {
        if (this.userSet == null) {
            this.userSet = new UserSet(this);
        }
        return this.userSet;
    }

    public RoleSet getRoleSet() {
        if (this.roleSet == null) {
            this.roleSet = new RoleSet(this);
        }
        return roleSet;
    }

    public UserRoleRLSet getUserRoleRLSet() {
        if (this.userRoleRLSet == null) {
            this.userRoleRLSet = new UserRoleRLSet(this);
        }
        return userRoleRLSet;
    }

    public CaptchaSet getCaptchaSet() {
        if (this.captchaSet == null) {
            this.captchaSet = new CaptchaSet(this);
        }
        return captchaSet;
    }

    public OAuthWeixinUserSet getOAuthWeixinUserSet() {
        if (this.oAuthWeixinUserSet == null) {
            this.oAuthWeixinUserSet = new OAuthWeixinUserSet(this);
        }
        return oAuthWeixinUserSet;
    }
}
