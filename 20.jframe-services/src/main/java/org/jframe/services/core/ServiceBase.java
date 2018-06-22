package org.jframe.services.core;

import org.jframe.core.extensions.Action1;
import org.jframe.data.JframeDbContext;

import java.util.function.Function;

/**
 * Created by screw on 2017/5/19.
 */
public abstract class ServiceBase {
    protected <T> T getFromDb(Function<JframeDbContext, T> func) {
        try (JframeDbContext db = new JframeDbContext()) {
            return func.apply(db);
        }
    }

    protected void useTransaction(Action1<JframeDbContext> action) {
        try (JframeDbContext db = new JframeDbContext(true)) {
            action.apply(db);
            db.commitTransaction();
        }
    }

}
