package org.jframe.infrastructure.mongodb;

import org.jframe.core.app.AppInitializer;
import org.jframe.core.mongodb.MongoDatabaseFactory;
import org.jframe.core.mongodb.MongoDbContext;
import org.jframe.core.mongodb.MongodbConfig;
import org.jframe.infrastructure.AppContext;

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

    public MongoDbContext createLogsBackupDb() {
        return new MongoDbContext(this, AppContext.getAppConfig().getLogsDbName() + "_backup");
    }

    public MongoDbContext createLogsDb() {
        return new MongoDbContext(this, AppContext.getAppConfig().getLogsDbName());
    }

    @Override
    public String init() {
        MongodbConfig config = AppContext.getMongodbConfig();
        super.initialize(config);
        return "MongoDB [" + config.getAddress() + "/" + config.getDatabase() + "] initialized.";
    }
}
