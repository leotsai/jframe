package org.jframe.services.logging;

import org.jframe.core.extensions.JDate;
import org.jframe.core.logging.appenders._LogAppenderBase;
import org.jframe.core.logging.enums.LogArea;
import org.jframe.data.entities.LogMySql;
import org.jframe.infrastructure.AppContext;
import org.jframe.infrastructure.helpers.DateHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
            this.writeFile(vectors);
            vectors.clear();
        }
    }

    private void writeFile(Vector<LogMySql> vectors) {
        String logPath = AppContext.getAppConfig().getFileLogPath();
        File file = new File(logPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        logPath += DateHelper.toDate(JDate.now()) + ".txt";
        file = new File(logPath);
        OutputStreamWriter out;
        try {
            out = new OutputStreamWriter(new FileOutputStream(file, true), "utf-8");
            for (LogMySql log : vectors) {
                out.write(log.formatFileLog());
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
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
