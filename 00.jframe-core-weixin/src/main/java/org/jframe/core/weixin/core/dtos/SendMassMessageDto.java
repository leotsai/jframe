package org.jframe.core.weixin.core.dtos;

/**
 * Created by leo on 2017-09-24.
 */
public class SendMassMessageDto extends _ApiDtoBase {

    private long msg_id ;
    private long msg_data_id ;

    public long getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(long msg_id) {
        this.msg_id = msg_id;
    }

    public long getMsg_data_id() {
        return msg_data_id;
    }

    public void setMsg_data_id(long msg_data_id) {
        this.msg_data_id = msg_data_id;
    }
}
