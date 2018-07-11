package org.jframe.services.core;

import org.jframe.core.extensions.Action1;
import org.jframe.data.JframeDbContext;

import java.util.function.Function;

/**
 * Created by screw on 2017/5/19.
 */
public abstract class ServiceBase {

    /**
     * 从数据库查询数据，没有事务
     *
     * @param function
     * @param <R>
     * @return
     */
    protected <R> R getFromDb(Function<JframeDbContext, R> function) {
        try (JframeDbContext db = new JframeDbContext()) {
            return function.apply(db);
        }
    }

    /**
     * 事务操作，没有返回值
     *
     * @param action
     */
    protected void useTransaction(Action1<JframeDbContext> action) {
        try (JframeDbContext db = new JframeDbContext(true)) {
            action.apply(db);
            db.commitTransaction();
        }
    }

    /**
     * 事务操作，有返回值
     *
     * @param function
     * @param <R>
     * @return
     */
    protected <R> R useTransaction(Function<JframeDbContext, R> function) {
        try (JframeDbContext db = new JframeDbContext(true)) {
            R result = function.apply(db);
            db.commitTransaction();
            return result;
        }
    }

}
