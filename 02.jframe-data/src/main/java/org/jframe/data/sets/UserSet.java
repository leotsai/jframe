package org.jframe.data.sets;

import org.jframe.data.entities.User;
import org.jframe.infrastructure.data.DbContext;
import org.jframe.infrastructure.data.DbSet;

/**
 * Created by leo on 2017-05-31.
 */
public class UserSet extends DbSet<User>{

    public UserSet(DbContext db) {
        super(db, User.class);
    }

    public boolean existsUsername(String username) {
        return super.count("where username=:p0", username) > 0;
    }

}
