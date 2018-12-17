package org.jframe.core.logging.appenders;



import org.jframe.core.extensions.JDate;
import org.jframe.core.logging.LogHelper;
import org.jframe.core.logging.enums.LogArea;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Paths;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by leo on 2017-08-21.
 */
public abstract class FileLogAppender extends _LogAppenderBase {
    private final ConcurrentHashMap<String, Vector<String>> warn;
    private final ConcurrentHashMap<String, Vector<String>> error;
    private final ConcurrentHashMap<String, Vector<String>> fatal;

    public FileLogAppender() {
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

    private void entryArea(ConcurrentHashMap<String, Vector<String>> area, String group, String message) {
        if (!area.containsKey(group)) {
            area.put(group, new Vector<>());
        }
        area.get(group).add(message);
    }

    private void writeToDb(LogArea area, ConcurrentHashMap<String, Vector<String>> map) {
        for (String key : map.keySet()) {
            Vector<String> list = map.get(key);
            if (list.isEmpty()) {
                continue;
            }

            File file = this.getLogFile(area, key, 0);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            OutputStreamWriter out;
            try {
                out = new OutputStreamWriter(new FileOutputStream(file, true), "utf-8");
                for (String log : list) {
                    out.write(LogHelper.getLineBreak() + LogHelper.getLineBreak() + log);
                }
                out.flush();
                out.close();
            } catch (IOException e) {
                LogHelper.error().tryPrintExceptionIfConfigured(e);
            }
            list.clear();
        }
    }

    private File getLogFile(LogArea area, String group, Integer index) {
        String relative = area + "/" + group + "/" + JDate.now().toString("yyyy-MM-dd") + (index.equals(0) ? "" : "-" + index) + ".log";
        File file = Paths.get(this.getLogDirectory(), relative).toFile();
        if (file.exists() && file.length() > 4 * 1024 * 1024) {
            return getLogFile(area, group, index + 1);
        }
        return file;
    }

    protected abstract String getLogDirectory();

}
