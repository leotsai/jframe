package org.jframe.data.sets;

import org.jframe.core.hibernate.DbContext;
import org.jframe.core.hibernate.DbSet;
import org.jframe.data.entities.Article;

/**
 * created by yezi on 2018/7/3
 */
public class ArticleSet extends DbSet<Article> {

    public ArticleSet(DbContext db) {
        super(db, Article.class);
    }

}
