package org.jframe.core.weixin.messaging.core;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jframe.core.weixin.core.helpers.XmlHelper;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Leo on 2017/10/30.
 * 官方文档 https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140453
 * https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1433751277
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "MessageResult")
public class MessageResult implements Serializable{
    @XmlElement(name = "ToUserName")
    private String toUserName ;//开发者微信号

    @XmlElement(name = "FromUserName")
    private String fromUserName ;//发送方帐号（一个OpenID）

    @XmlElement(name = "CreateTime")
    private Long createTime ;//消息创建时间 （整型）

    @XmlElement(name = "MsgType")
    private MessageType msgType ;

    @XmlElement(name = "Content")
    private String content ;//文本消息内容

    @XmlElement(name = "PicUrl")
    private String picUrl ;//图片链接（由系统生成）

    @XmlElement(name = "MediaId")
    private String mediaId ;//图片/语音/视频消息媒体id，可以调用多媒体文件下载接口拉取数据。

    @XmlElement(name = "MsgId")
    private Long msgId;//消息id，64位整型

    @XmlElement(name = "Format")
    private String format ;//语音格式，如amr，speex等

    @XmlElement(name = "Recognition")
    private String recognition ;//语音识别结果，UTF8编码

    @XmlElement(name = "ThumbMediaId")
    private String thumbMediaId ;//视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。

    @XmlElement(name = "Location_X")
    private Double location_x ;//地理位置维度

    @XmlElement(name = "Location_Y")
    private Double location_y ;//地理位置经度

    @XmlElement(name = "Scale")
    private Double scale ;//地图缩放大小

    @XmlElement(name = "Label")
    private String label ;//地理位置信息

    @XmlElement(name = "Title")
    private String title ;//链接消息标题

    @XmlElement(name = "Description")
    private String description ;//链接消息描述

    @XmlElement(name = "Url")
    private String url ;//链接消息链接


    @XmlElement(name = "Event")
    private MessageEventType event ;

    @XmlElement(name = "EventKey")
    private String eventKey ;

    @XmlElement(name = "Ticket")
    private String ticket ;


    @XmlElement(name = "Latitude")
    private String latitude ;

    @XmlElement(name = "Longitude")
    private String longitude ;

    @XmlElement(name = "Precision")
    private String precision ;

    @XmlElement(name = "Status")
    private String status ;//在模版消息发送任务完成后，微信服务器会将是否送达成功作为通知，发送到开发者中心中填写的服务器配置地址中。


    public static MessageResult fromXml(String xmlRaw) throws Exception
    {
        return XmlHelper.fromWeixinXml(MessageResult.class, xmlRaw);
    }


    //-------------------------------------------------------------

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public MessageType getMsgType() {
        return msgType;
    }

    public void setMsgType(MessageType msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getRecognition() {
        return recognition;
    }

    public void setRecognition(String recognition) {
        this.recognition = recognition;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }

    public Double getScale() {
        return scale;
    }

    public void setScale(Double scale) {
        this.scale = scale;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MessageEventType getEvent() {
        return event;
    }

    public void setEvent(MessageEventType event) {
        this.event = event;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPrecision() {
        return precision;
    }

    public void setPrecision(String precision) {
        this.precision = precision;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public Double getLocation_x() {
        return location_x;
    }

    public void setLocation_x(Double location_x) {
        this.location_x = location_x;
    }

    public Double getLocation_y() {
        return location_y;
    }

    public void setLocation_y(Double location_y) {
        this.location_y = location_y;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("toUserName", toUserName)
                .append("fromUserName", fromUserName)
                .append("createTime", createTime)
                .append("msgType", msgType)
                .append("content", content)
                .append("picUrl", picUrl)
                .append("mediaId", mediaId)
                .append("msgId", msgId)
                .append("format", format)
                .append("recognition", recognition)
                .append("thumbMediaId", thumbMediaId)
                .append("location_x", location_x)
                .append("location_y", location_y)
                .append("scale", scale)
                .append("label", label)
                .append("title", title)
                .append("description", description)
                .append("url", url)
                .append("event", event)
                .append("eventKey", eventKey)
                .append("ticket", ticket)
                .append("latitude", latitude)
                .append("longitude", longitude)
                .append("precision", precision)
                .append("status", status)
                .toString();
    }
}
