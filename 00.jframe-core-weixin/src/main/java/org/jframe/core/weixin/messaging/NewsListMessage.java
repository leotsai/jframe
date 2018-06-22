package org.jframe.core.weixin.messaging;

import org.jframe.core.helpers.JsonHelper;
import org.jframe.core.weixin.messaging.core.MessageBase;
import org.jframe.core.weixin.messaging.core.NewsMessageItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 2017/10/31.
 */
public class NewsListMessage extends MessageBase {
    private final List<NewsMessageItem> items ;

    public NewsListMessage(String openId)
    {
        super(openId);
        this.items = new ArrayList<>();
    }

    public void add(NewsMessageItem item)
    {
        this.items.add(item);
    }


    @Override
    public String toJson() {
        JsonDto dto = new JsonDto();
        dto.touser = this.getToUserOpenId();
        dto.news.articles = this.items;
        return JsonHelper.serialize(dto);
    }

    public List<NewsMessageItem> getItems() {
        return items;
    }


    private class JsonDto{
        private String touser;
        private String msgtype = "news";
        private JsonDtoArticles news = new JsonDtoArticles();

        public String getTouser() {
            return touser;
        }

        public String getMsgtype() {
            return msgtype;
        }
    }

    private class  JsonDtoArticles{
        private List<NewsMessageItem> articles;

        public List<NewsMessageItem> getArticles() {
            return articles;
        }
    }

}
