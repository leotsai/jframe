package org.jframe.data.entities;

import org.jframe.data.core.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * created by yezi on 2018/7/2
 */
@Entity
@Table(name = "s_articles")
public class Article extends EntityBase {

    @Column(name = "title", columnDefinition = "varchar(255) not null COMMENT '文章标题'")
    private String title;

    @Column(name = "keywords", columnDefinition = "longtext null COMMENT '关键字'")
    private String keywords;

    @Column(name = "url_key", columnDefinition = "varchar(255) not null COMMENT 'URL标识'")
    private String urlKey;

    @Column(name = "description", columnDefinition = "longtext null COMMENT '描述'")
    private String description;

    @Column(name = "pv", columnDefinition = "int not null COMMENT '阅读量'")
    private int pv = 0;

    @Column(name = "author", columnDefinition = "longtext null COMMENT '作者'")
    private String author;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
