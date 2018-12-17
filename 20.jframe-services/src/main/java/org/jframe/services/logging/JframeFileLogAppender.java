package org.jframe.services.logging;

import org.jframe.core.extensions.JDate;
import org.jframe.core.logging.appenders.FileLogAppender;
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
public class JframeFileLogAppender extends FileLogAppender {

    private final String logDirectory;

    public JframeFileLogAppender(String logDirectory) {
        this.logDirectory = logDirectory;
    }

    @Override
    protected String getLogDirectory() {
        return this.logDirectory;
    }
}
