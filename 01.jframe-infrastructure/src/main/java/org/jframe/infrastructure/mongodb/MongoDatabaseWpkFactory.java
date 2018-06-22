package org.jframe.infrastructure.mongodb;

import org.jframe.infrastructure.AppContext;
import org.jframe.core.mongodb.MongoDatabaseFactory;
import org.jframe.core.mongodb.MongoDbContext;

/**
 * Created by leo on 2017-10-22.
 */
public class MongoDatabaseWpkFactory extends MongoDatabaseFactory {

    private final static MongoDatabaseWpkFactory instance = new MongoDatabaseWpkFactory();

    public static MongoDatabaseWpkFactory getInstance(){
        return instance;
    }

    public void initialize(){
        super.initialize(AppContext.getMongodbConfig());
    }

    public MongoDbContext createLogsBackupDb() {
        return new MongoDbContext(this, AppContext.getAppConfig().getLogsDbName() + "_backup");
    }

    public MongoDbContext createLogsDb(){
        return new MongoDbContext(this, AppContext.getAppConfig().getLogsDbName());
    }

}
