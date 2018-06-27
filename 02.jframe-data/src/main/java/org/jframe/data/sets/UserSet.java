package org.jframe.data.sets;


import org.jframe.core.hibernate.DbContext;
import org.jframe.core.hibernate.DbSet;
import org.jframe.data.entities.User;

/**
 * Created by leo on 2017-05-31.
 */
public class UserSet extends DbSet<User> {

    public UserSet(DbContext db) {
        super(db, User.class);
    }

    public User get(String username) {
        return this.getFirst("where username=:p0", username);
    }

    public User getByWeixinOpenId(String openId) {
        String sql = "select u.* from s_oauth_weixin_users as w inner join s_users as u on u.id = w.user_id where w.open_id=:p0";
        return super.db.getFirst(sql, User.class, openId);
    }
}
