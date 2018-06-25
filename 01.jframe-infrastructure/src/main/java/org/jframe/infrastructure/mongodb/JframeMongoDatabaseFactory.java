package org.jframe.infrastructure.mongodb;

import org.jframe.core.app.AppInitializer;
import org.jframe.infrastructure.AppContext;
import org.jframe.core.mongodb.MongoDatabaseFactory;
import org.jframe.core.mongodb.MongoDbContext;

/**
 * Created by leo on 2017-10-22.
 */
public class JframeMongoDatabaseFactory extends MongoDatabaseFactory implements AppInitializer {

    private final static JframeMongoDatabaseFactory instance = new JframeMongoDatabaseFactory();

    public static JframeMongoDatabaseFactory getInstance() {
        return instance;
    }

    private JframeMongoDatabaseFactory() {
    }

    @Override
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
