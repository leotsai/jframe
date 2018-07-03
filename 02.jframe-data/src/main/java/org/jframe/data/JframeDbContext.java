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
    private DbCacheSet dbCacheSet;
    private EmployeeSet employeeSet;
    private PermissionSet permissionSet;
    private DepartmentSet departmentSet;
    private ArticleSet articleSet;

    public JframeDbContext() {
        super(JframeHibernateSessionFactory.getInstance());
    }

    public JframeDbContext(boolean transactional) {
        super(JframeHibernateSessionFactory.getInstance(), transactional);
    }

    public ArticleSet getArticleSet() {
        if (this.articleSet == null) {
            this.articleSet = new ArticleSet(this);
        }
        return this.articleSet;
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

    public DbCacheSet getDbCacheSet() {
        if (this.dbCacheSet == null) {
            this.dbCacheSet = new DbCacheSet(this);
        }
        return dbCacheSet;
    }

    public EmployeeSet getEmployeeSet() {
        if (this.employeeSet == null) {
            this.employeeSet = new EmployeeSet(this);
        }
        return employeeSet;
    }

    public PermissionSet getPermissionSet() {
        if (this.permissionSet == null) {
            this.permissionSet = new PermissionSet(this);
        }
        return permissionSet;
    }

    public DepartmentSet getDepartmentSet() {
        if (this.departmentSet == null) {
            this.departmentSet = new DepartmentSet(this);
        }
        return departmentSet;
    }
}
