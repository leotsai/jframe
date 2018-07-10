package org.jframe.services.logging;

import org.jframe.core.extensions.JList;
import org.jframe.core.logging.appenders._LogAppenderBase;
import org.jframe.core.logging.enums.LogArea;
import org.jframe.data.entities.LogMySql;
import org.jframe.infrastructure.AppContext;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * created by yezi on 2018/7/10
 */
public class JframeFileLogAppender extends _LogAppenderBase {

    private final boolean autoAppendHttpHeaders;
    private final ConcurrentHashMap<String, Vector<LogMySql>> map;

    public JframeFileLogAppender(boolean autoAppendHttpHeaders) {
        this.autoAppendHttpHeaders = autoAppendHttpHeaders;
        this.map = new ConcurrentHashMap<>();
    }

    @Override
    public void entry(String group, String message, int type, String location) {
        if (!this.map.containsKey(group)) {
            this.map.put(group, new Vector<>());
        }
        LogMySql logMySql = new LogMySql(group, message, LogArea.from(type), location);
        this.map.get(group).add(logMySql);
    }

    @Override
    public void doWork() {
        this.writeToFile();
    }

    private void writeToFile() {
        for (String key : this.map.keySet()) {
            Vector<LogMySql> vectors = this.map.get(key);
            if (vectors.isEmpty()) {
                continue;
            }
            JList<LogMySql> logs = JList.from(vectors);
            //todo 日志写入文件

            vectors.clear();
        }
    }

    @Override
    public boolean printStackTrace() {
        return AppContext.getAppConfig().isPrintStackTrace();
    }

    @Override
    public String getServerName() {
        return AppContext.getAppConfig().getServerName();
    }

    @Override
    public boolean autoAppendHttpHeaders() {
        return this.autoAppendHttpHeaders;
    }

    @Override
    public void stop() {
        super.stop();
    }
}
