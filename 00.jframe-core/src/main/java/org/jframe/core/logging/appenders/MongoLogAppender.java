package org.jframe.core.logging.appenders;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.jframe.core.extensions.JDate;
import org.jframe.core.mongodb.MongoDbContext;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by leo on 2017-08-21.
 */
public abstract class MongoLogAppender extends _LogAppenderBase{
    private final ConcurrentHashMap<String, Vector<Document>> map;

    public MongoLogAppender() {
        this.map = new ConcurrentHashMap<>();
    }

    @Override
    public void doWork(){
        this.writeToDb();
    }

    @Override
    public void entry(String group, String message) {
        if (!this.map.containsKey(group)) {
            this.map.put(group, new Vector<>());
        }
        JDate now = JDate.now();
        Document document = new Document("year-month", now.toString("yyyy-MM"))
                .append("date", now.toDateString())
                .append("time", now.toDateTimeString())
                .append("log", message);
        this.map.get(group).add(document);
    }

    private void writeToDb() {
        MongoDbContext dbContext = this.getMongoDbContext();
        for (String key : this.map.keySet()) {
            Vector<Document> vector = this.map.get(key);
            if (vector.isEmpty()) {
                continue;
            }
            MongoCollection<Document> collection = dbContext.getDatabase().getCollection(key);
            collection.insertMany(vector);
            vector.clear();
        }
    }

    protected abstract MongoDbContext getMongoDbContext();

}
