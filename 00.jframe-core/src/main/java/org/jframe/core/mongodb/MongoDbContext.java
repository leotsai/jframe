package org.jframe.core.mongodb;

import com.mongodb.client.MongoDatabase;

/**
 * Created by leo on 2017-08-21.
 */
public class MongoDbContext{

    private final MongoDatabase database;
    private final MongoDatabaseFactory factory;

    public MongoDbContext(MongoDatabaseFactory factory, String databaseName){
        this.factory = factory;
        this.database = factory.getDatabase(databaseName);
    }

    public MongoDatabase getDatabase(){
        return this.database;
    }

}
