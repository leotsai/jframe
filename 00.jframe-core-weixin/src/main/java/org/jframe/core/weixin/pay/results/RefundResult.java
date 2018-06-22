package org.jframe.core.weixin.pay.results;


import org.jframe.core.extensions.JMap;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Leo on 2017/10/19.
 * https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_4
 */

public class RefundResult extends _ApiResultBase implements Serializable {

    private String result_code;
    private String err_code;
    private String err_code_des;

    public boolean isRefundSuccess() {
        return super.isHttpSuccess() && "SUCCESS".equals(this.result_code);
    }

    @Override
    public RefundResult loadFromResponse(Map<String, String> response) {
        super.loadFromResponse(response);
        if(super.isHttpSuccess() == false){
            return this;
        }
        JMap<String, String> jmap = new JMap<>(response);
        this.result_code = jmap.get("result_code");
        this.err_code = jmap.get("err_code");
        this.err_code_des = jmap.get("err_code_des");
        return this;
    }

    public String getResult_code() {
        return result_code;
    }

    public String getErr_code() {
        return err_code;
    }

    public String getErr_code_des() {
        return err_code_des;
    }

    //=======================================================================================


}
