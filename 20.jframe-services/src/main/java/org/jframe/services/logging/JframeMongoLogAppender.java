package org.jframe.services.logging;

import org.jframe.core.logging.appenders.MongoLogAppender;
import org.jframe.core.logging.enums.LogArea;
import org.jframe.core.mongodb.MongoDbContext;
import org.jframe.infrastructure.AppContext;
import org.jframe.infrastructure.mongodb.JframeMongoDatabaseFactory;

/**
 * Created by leo on 2017-10-22.
 */
public class JframeMongoLogAppender extends MongoLogAppender {

    private final String databaseName;

    public JframeMongoLogAppender(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    protected MongoDbContext getMongoDbContext(LogArea area) {
        return new MongoDbContext(JframeMongoDatabaseFactory.getInstance(), getDbName(this.databaseName, area));
    }

    @Override
    public void stop() {
        super.stop();
        JframeMongoDatabaseFactory.getInstance().close();
    }

    public static String getDbName(String databaseName, LogArea area){
        return databaseName+"_"+area;
    }

}
