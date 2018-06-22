package org.jframe.core.weixin.core.dtos;

/**
 * Created by leo on 2017-09-23.
 */
public class _ApiDtoBase {
    private int errcode;
    private String errmsg;

    public boolean isSuccess(){
        return this.errcode == 0;
    }

    public String getFullError(){
        return this.errcode +": " + this.errmsg;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
