package org.jframe.core.logging.appenders;



import org.jframe.core.logging.LogAppender;
import org.jframe.core.logging.LogHelper;
import org.jframe.core.logging.LoggingConfig;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by leo on 2017-08-22.
 */
public abstract class _LogAppenderBase implements LogAppender {
    private final ScheduledThreadPoolExecutor pool;
    private boolean isStarted;

    public _LogAppenderBase() {
        this.pool = new ScheduledThreadPoolExecutor(1);
    }

    @Override
    public boolean isStarted() {
        return this.isStarted;
    }

    @Override
    public void start() {
        if (this.isStarted) {
            return;
        }
        LoggingConfig config = LogHelper.getConfig();
        this.pool.scheduleAtFixedRate(() -> onTimerElapsed(), config.getInitialDelaySeconds(), config.getFlushIntervalSeconds(), TimeUnit.SECONDS);
        this.isStarted = true;
    }

    @Override
    public void stop() {
        this.pool.shutdown();
        this.isStarted = false;
    }

    private void onTimerElapsed() {
        try {
            this.doWork();
        } catch (Exception ex1) {
            LogHelper.error().tryPrintExceptionIfConfigured(ex1);
        }
    }

    public abstract void doWork();

}
