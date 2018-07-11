package org.jframe.infrastructure.sms;

import org.jframe.core.aliyun.sms.AliyunSmsApi;
import org.jframe.core.aliyun.sms.SmsTemplate;
import org.jframe.core.app.AppInitializer;
import org.jframe.infrastructure.AppContext;
import org.jframe.infrastructure.sms.templates.*;

/**
 * Created by leo on 2017-11-12.
 */
public class JframeSmsApi extends AliyunSmsApi implements AppInitializer {

    private static final JframeSmsApi instance = new JframeSmsApi();

    public static final JframeSmsApi getInstance() {
        return instance;
    }

    private JframeSmsApi() {

    }

    @Override
    public String init() {
        super.initialize(AppContext.getSmsConfig());
        return this.getClass().getName() + " initialize success!";
    }

    @Override
    public void close() {

    }

    @Override
    public void trySend(SmsTemplate template) {
        if (AppContext.getAppConfig().isDeveloperMode()) {
            System.out.println(template.toJson());
            return;
        }
        super.trySend(template);
    }

}
