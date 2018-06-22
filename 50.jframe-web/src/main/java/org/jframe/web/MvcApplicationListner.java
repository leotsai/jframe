package org.jframe.web;

import org.jframe.core.logging.LogHelper;
import org.jframe.infrastructure.AppContext;
import org.jframe.infrastructure.logging.MongoWpkLogAppender;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextStartedEvent;

/**
 * Created by leo on 2017-06-28.
 */
public class MvcApplicationListner implements ApplicationListener {
    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if(applicationEvent instanceof ContextClosedEvent){
            LogHelper.stopLogger();
            System.out.print("\nlogger stopped");
        }
        else if(applicationEvent instanceof ContextStartedEvent){
            LogHelper.startLogger(new MongoWpkLogAppender(AppContext.getAppConfig().getLogsDbName()), true);
            System.out.print("\nlogger started");
        }
    }
}
