package org.jframe.core.mongodb;

/**
 * Created by leo on 2017-10-21.
 */
public interface MongodbConfig {
     String getAddress();

     int getPort();

     String getUsername();

     String getPassword();

     String getDatabase();
}
