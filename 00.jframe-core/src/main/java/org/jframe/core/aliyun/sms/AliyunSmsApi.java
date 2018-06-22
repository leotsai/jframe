package org.jframe.core.aliyun.sms;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.jframe.core.extensions.KnownException;
import org.jframe.core.helpers.JsonHelper;
import org.jframe.core.logging.LogHelper;

/**
 * Created by leo on 2017-11-12.
 */
public class AliyunSmsApi {

    private SmsConfig config;

    public void initialize(SmsConfig config) {
        this.config = config;
        System.out.println("aliyun SMS API initialized");
    }

    public void trySend(SmsTemplate template) {
        try {
            this.send(template);
        } catch (Exception ex) {
            LogHelper.log("aliyunsms.trysend", ex);
            if (ex instanceof KnownException) {
                throw new KnownException(ex.getMessage());
            }
            throw new KnownException("发送短信验证码出错了，请重试");
        }
    }

    public void send(SmsTemplate template) throws Exception {
        if (this.config.getKey().equals("LOCAL_DEV")) {
            return;
        }
        IClientProfile profile = DefaultProfile.getProfile(SmsConfig.REGION_ID, this.config.getKey(), this.config.getSecret());
        DefaultProfile.addEndpoint(SmsConfig.ENDPOINT_NAME, SmsConfig.REGION_ID, SmsConfig.PRODUCT, SmsConfig.DOMAIN);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(template.getPhoneNumbers().joinString(",", x -> x));
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(template.getSignName());
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(template.getTemplateCode());
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam(template.toJson());

        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("yourOutId");

        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        if (!"OK".equals(sendSmsResponse.getCode())) {
            LogHelper.log("aliyunsms.send", JsonHelper.serialize(sendSmsResponse));
            String message = "发送短信验证码出错了，请重试";
            if ("isv.BUSINESS_LIMIT_CONTROL".equals(sendSmsResponse.getCode())) {
                message = "您获取短信验证码的频率有点高了，请过1分钟后再试试吧";
            }
            throw new KnownException(message);
        }
    }

}
