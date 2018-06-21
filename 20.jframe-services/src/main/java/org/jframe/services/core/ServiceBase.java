package org.jframe.services.core;

import org.jframe.data.JframeDbContext;
import org.jframe.infrastructure.extensions.SafeAction1;
import org.jframe.infrastructure.extensions.SafeFunc1;

/**
 * Created by screw on 2017/5/19.
 */
public abstract class ServiceBase {
    protected <T> T getFromDb(SafeFunc1<JframeDbContext, T> func){
        JframeDbContext db = new JframeDbContext();
        T result = func.invoke(db);
        db.tryClose();
        return result;
    }

    protected void useTransaction(SafeAction1<JframeDbContext> action){
        JframeDbContext db = new JframeDbContext();
        action.invoke(db);
        db.commitTransaction();
        db.tryClose();
    }

}
