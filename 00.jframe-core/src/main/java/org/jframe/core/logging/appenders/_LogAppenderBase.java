package org.jframe.core.logging.appenders;

import org.jframe.core.logging.LogAppender;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by leo on 2017-08-22.
 */
public abstract class _LogAppenderBase implements LogAppender{
    private int intervalSeconds = 5;
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
    public void start(){
        if(this.isStarted){
            return;
        }
        this.pool.scheduleAtFixedRate(() -> onTimerElapsed(), this.intervalSeconds, this.intervalSeconds, TimeUnit.SECONDS);
        this.isStarted = true;
    }

    @Override
    public void stop(){
        this.pool.shutdown();
        this.isStarted = false;
    }

    private void onTimerElapsed(){
        try{
            this.doWork();
        }
        catch (Exception ex1){
            try{
                if(this.printStackTrace()){
                    ex1.printStackTrace();
                }
            }
            catch (Exception ex2){

            }
        }
    }
    public abstract void doWork();

    public int getIntervalSeconds() {
        return intervalSeconds;
    }

    public void setIntervalSeconds(int intervalSeconds) {
        this.intervalSeconds = intervalSeconds;
    }
}
