package org.jframe.core.weixin.core.parameters;

/**
 * Created by leo on 2017-09-24.
 */
public class SendMassMessageParameter {
    private Filter filter = new Filter();
    private MpNews mpnews = new MpNews();
    private String msgtype;
    private int send_ignore_reprint;

    public SendMassMessageParameter(){

    }

    public SendMassMessageParameter(Integer tagId, String msgtype){
        this.msgtype = msgtype;
        this.filter.setIs_to_all(tagId == null);
        this.filter.setTag_id(tagId.intValue());
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public MpNews getMpnews() {
        return mpnews;
    }

    public void setMpnews(MpNews mpnews) {
        this.mpnews = mpnews;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public int getSend_ignore_reprint() {
        return send_ignore_reprint;
    }

    public void setSend_ignore_reprint(int send_ignore_reprint) {
        this.send_ignore_reprint = send_ignore_reprint;
    }

    private class Filter{
        private boolean is_to_all;
        private int tag_id;

        public boolean isIs_to_all() {
            return is_to_all;
        }

        public void setIs_to_all(boolean is_to_all) {
            this.is_to_all = is_to_all;
        }

        public int getTag_id() {
            return tag_id;
        }

        public void setTag_id(int tag_id) {
            this.tag_id = tag_id;
        }
    }

    private class MpNews{
        private String media_id;

        public String getMedia_id() {
            return media_id;
        }

        public void setMedia_id(String media_id) {
            this.media_id = media_id;
        }
    }

}
