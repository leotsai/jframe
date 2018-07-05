package org.jframe.infrastructure.logging;

import org.jframe.core.logging.appenders.MongoLogAppender;
import org.jframe.core.mongodb.MongoDbContext;
import org.jframe.infrastructure.AppContext;
import org.jframe.infrastructure.mongodb.JframeMongoDatabaseFactory;

/**
 * Created by leo on 2017-10-22.
 */
public class JframeMongoLogAppender extends MongoLogAppender {

    private final String databaseName;
    private final boolean autoAppendHttpHeaders;

    public JframeMongoLogAppender(String databaseName, boolean autoAppendHttpHeaders) {
        super.setIntervalSeconds(5);
        this.databaseName = databaseName;
        this.autoAppendHttpHeaders = autoAppendHttpHeaders;
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
    public boolean autoAppendHttpHeaders() {
        return this.autoAppendHttpHeaders;
    }

    @Override
    protected MongoDbContext getMongoDbContext() {
        return new MongoDbContext(JframeMongoDatabaseFactory.getInstance(), this.databaseName);
    }


    @Override
    public void stop() {
        super.stop();
        JframeMongoDatabaseFactory.getInstance().close();
    }

}
