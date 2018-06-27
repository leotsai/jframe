package org.jframe.data.entities;

import org.jframe.core.extensions.JList;
import org.jframe.data.JframeDbContext;
import org.jframe.data.core.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Date;
import java.util.Objects;


/**
 * Created by Leo on 2017/10/23.
 */
@Entity
@Table(name = "s_images", indexes = {
        @Index(name = "idx_key", columnList = "key")
})
public class Image extends EntityBase {

    public static class Keys{
        public final static String UserDefaultAvatar="app.user.default.avatar";
    }

    @Column(name = "`key`", columnDefinition = "varchar(100) not null COMMENT '图片key'")
    private String key;

    @Column(name = "is_system", columnDefinition = "bool not null COMMENT '是否是系统图片，如果是不允许删除'")
    private boolean isSystem;

    @Column(name = "notes", columnDefinition = "varchar(250) null COMMENT '备注'")
    private String notes;

    @Column(name = "last_updated_time", columnDefinition = "datetime null COMMENT '最近更新时间'")
    private Date lastUpdatedTime;

    //-------------------------------------------------------------------------------------

    public Image() {

    }

    public Image(String key, String notes, boolean isSystem) {
        this.key = key;
        this.notes = notes;
        this.isSystem = isSystem;
    }

    public static JList<Image> getSystemImages() {
        JList<Image> list = new JList<>();
        list.add(new Image(Keys.UserDefaultAvatar, "用户默认头像，100*100", true));
        return list;
    }

    public static void initSystemImages() {
        try (JframeDbContext db = new JframeDbContext()) {
            JList<Image> dbImages = db.set(Image.class).getAll().where(x -> x.isSystem());
            JList<Image> requiredImages = getSystemImages();
            JList<Image> addedList = requiredImages.where(x -> !dbImages.any(d -> d.getKey().equals(x.getKey())));
            JList<Image> deletedList = dbImages.where(d -> !requiredImages.any(r -> r.getKey().equals(d.getKey())));
            JList<Image> updatedImages = dbImages.where(d -> requiredImages.any(r -> r.getKey().equals(d.getKey())));
            addedList.forEach(item -> db.save(item));
            deletedList.forEach(item -> db.delete(item));
            int updated = 0;
            for (Image dbItem : updatedImages) {
                Image newItem = requiredImages.firstOrNull(x -> x.getKey().equals(dbItem.getKey()));
                if (!Objects.equals(dbItem.getNotes(), newItem.getNotes())) {
                    dbItem.setNotes(newItem.getNotes());
                    dbItem.setLastUpdatedTime(new Date());
                    db.save(dbItem);
                    updated++;
                }
            }
            db.commitTransaction();
            System.out.println("init images done. added: " + addedList.size() + ", deleted: " + deletedList.size() + ", updated: " + updated);
        }

    }


    //-------------------------------------------------------------------------------------


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }
}
