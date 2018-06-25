package org.jframe.infrastructure.configs;

import org.jframe.core.mongodb.MongodbConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by Leo on 2017/10/20.
 */
@Component
@PropertySource("/WEB-INF/app.properties")
public class JframeMongodbConfig implements MongodbConfig {

    @Value("${mongo.address}")
    private String address;

    @Value("${mongo.port}")
    private int port;

    @Value("${mongo.database}")
    private String database;

    @Value("${mongo.username}")
    private String username;

    @Value("${mongo.password}")
    private String password;

    //--------------------------------------------

    @Override
    public String getAddress() {
        return this.address;
    }

    @Override
    public int getPort() {
        return this.port;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getDatabase() {
        return this.database;
    }
}
