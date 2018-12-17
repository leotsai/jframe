package org.jframe.core.logging.appenders;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.jframe.core.extensions.JDate;
import org.jframe.core.logging.enums.LogArea;
import org.jframe.core.mongodb.MongoDbContext;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by leo on 2017-08-21.
 */
public abstract class MongoLogAppender extends _LogAppenderBase {
    private final ConcurrentHashMap<String, Vector<Document>> warn;
    private final ConcurrentHashMap<String, Vector<Document>> error;
    private final ConcurrentHashMap<String, Vector<Document>> fatal;

    public MongoLogAppender() {
        this.warn = new ConcurrentHashMap<>();
        this.error = new ConcurrentHashMap<>();
        this.fatal = new ConcurrentHashMap<>();
    }

    @Override
    public void doWork() {
        this.writeToDb(LogArea.INFO, this.warn);
        this.writeToDb(LogArea.ERROR, this.error);
        this.writeToDb(LogArea.FATAL, this.fatal);
    }

    @Override
    public void entry(LogArea area, String group, String message) {
        switch (area) {
            case INFO:
                this.entryArea(this.warn, group, message);
                break;
            case ERROR:
                this.entryArea(this.error, group, message);
                break;
            case FATAL:
                this.entryArea(this.fatal, group, message);
                break;
        }
    }

    private void entryArea(ConcurrentHashMap<String, Vector<Document>> area, String group, String message) {
        if (!area.containsKey(group)) {
            area.put(group, new Vector<>());
        }
        JDate now = JDate.now();
        Document document = new Document("year-month", now.toString("yyyy-MM"))
                .append("date", now.toDateString())
                .append("time", now.toDateTimeString())
                .append("log", message);
        area.get(group).add(document);
    }

    private void writeToDb(LogArea area, ConcurrentHashMap<String, Vector<Document>> map) {
        MongoDbContext dbContext = this.getMongoDbContext(area);
        for (String key : map.keySet()) {
            Vector<Document> vector = map.get(key);
            if (vector.isEmpty()) {
                continue;
            }
            MongoCollection<Document> collection = dbContext.getDatabase().getCollection(key);
            collection.insertMany(vector);
            vector.clear();
        }
    }

    protected abstract MongoDbContext getMongoDbContext(LogArea area);

}
