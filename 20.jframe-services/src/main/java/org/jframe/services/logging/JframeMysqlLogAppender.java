package org.jframe.services.logging;

import org.jframe.core.extensions.JList;
import org.jframe.core.logging.appenders._LogAppenderBase;
import org.jframe.core.logging.enums.LogArea;
import org.jframe.data.JframeDbContext;
import org.jframe.data.entities.LogMySql;
import org.jframe.infrastructure.AppContext;

import java.sql.PreparedStatement;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * created by yezi on 2018/7/9
 */
public class JframeMysqlLogAppender extends _LogAppenderBase {

    private final ConcurrentHashMap<String, Vector<LogMySql>> map;
    private final boolean autoAppendHttpHeaders;

    public JframeMysqlLogAppender(boolean autoAppendHttpHeaders) {
        this.autoAppendHttpHeaders = autoAppendHttpHeaders;
        this.map = new ConcurrentHashMap<>();
    }

    @Override
    public void entry(LogArea area, String group, String message) {
        if (!this.map.containsKey(group)) {
            this.map.put(group, new Vector<>());
        }
        LogMySql logMySql = new LogMySql(group, message, area);
        this.map.get(group).add(logMySql);
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public void doWork() {
        this.writeToDb();
    }

    private void writeToDb() {
        try (JframeDbContext db = new JframeDbContext()) {
            for (String key : this.map.keySet()) {
                Vector<LogMySql> vectors = this.map.get(key);
                if (vectors.isEmpty()) {
                    continue;
                }
                JList<LogMySql> logs = JList.from(vectors);
                db.getSession().doWork(conn -> {
                    conn.setAutoCommit(false);
                    PreparedStatement statement = conn.prepareStatement("INSERT INTO `s_mysql_logs` (`created_time`, `group`, `location`, `log`, `type`) VALUES (?, ?, ?, ?, ?)");
                    for (int i = 0; i < logs.size(); i += 100) {
                        statement.clearBatch();
                        int x;
                        for (int j = 0; j < 100; j++) {
                            x = i + j;
                            if (logs.size() == x) {
                                return;
                            }
                            LogMySql logMySql = logs.get(x);
                            statement.setDate(1, new java.sql.Date(logMySql.getCreatedTime().getTime()));
                            statement.setString(2, logMySql.getGroup());
                            statement.setString(3, logMySql.getLocation());
                            statement.setString(4, logMySql.getLog());
                            statement.setInt(5, logMySql.getType().getValue());
                            statement.addBatch();
                        }
                        statement.executeBatch();
                        conn.commit();
                    }
                    conn.commit();
                });
                vectors.clear();
            }
        }
    }

}
