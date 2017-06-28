package org.jframe.data;

import org.jframe.data.sets.UserSet;
import org.jframe.infrastructure.data.DbContext;

/**
 * Created by leo on 2017-05-31.
 */
public class JframeDbContext extends DbContext{
    private UserSet userSet;

    public UserSet getUserSet(){
        if(this.userSet == null){
            this.userSet = new UserSet(this);
        }
        return this.userSet;
    }

}
