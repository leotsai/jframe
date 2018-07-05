package org.jframe.infrastructure.mongodb;

import org.jframe.core.mongodb.MongoDatabaseFactory;
import org.jframe.core.mongodb.MongoDbContext;
import org.jframe.infrastructure.AppContext;

/**
 * Created by leo on 2017-10-22.
 */
public class JframeMongoDatabaseFactory extends MongoDatabaseFactory {

    private final static JframeMongoDatabaseFactory instance = new JframeMongoDatabaseFactory();

    public static JframeMongoDatabaseFactory getInstance() {
        return instance;
    }

    private JframeMongoDatabaseFactory() {
    }

    public void initialize() {
        super.initialize(AppContext.getMongodbConfig());
    }

    public MongoDbContext createLogsBackupDb() {
        return new MongoDbContext(this, AppContext.getAppConfig().getLogsDbName() + "_backup");
    }

    public MongoDbContext createLogsDb() {
        return new MongoDbContext(this, AppContext.getAppConfig().getLogsDbName());
    }

}
