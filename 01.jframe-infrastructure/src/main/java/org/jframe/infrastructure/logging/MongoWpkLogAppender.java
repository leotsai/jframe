package org.jframe.infrastructure.logging;

import org.jframe.infrastructure.AppContext;
import org.jframe.infrastructure.mongodb.MongoDatabaseWpkFactory;
import org.jframe.core.logging.appenders.MongoLogAppender;
import org.jframe.core.mongodb.MongoDbContext;

/**
 * Created by leo on 2017-10-22.
 */
public class MongoWpkLogAppender extends MongoLogAppender {

    private final String databaseName;

    public MongoWpkLogAppender(String databaseName) {
        super.setIntervalSeconds(5);
        this.databaseName = databaseName;
    }

    @Override
    public boolean printStackTrace() {
        return AppContext.getAppConfig().isPrintStackTrace();
    }

    @Override
    public String getServerName() {
        return AppContext.getAppConfig().getServerName();
    }

    @Override
    protected MongoDbContext getMongoDbContext() {
        return new MongoDbContext(MongoDatabaseWpkFactory.getInstance(),this.databaseName);
    }


    @Override
    public void stop(){
        super.stop();
        MongoDatabaseWpkFactory.getInstance().close();
    }

}
