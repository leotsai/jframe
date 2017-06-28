package org.jframe.data.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;
import java.util.UUID;

/**
 * Created by leo on 2017-05-10.
 */
@MappedSuperclass
public class EntityBase {

    protected static final String EmojCharset = " CHARACTER SET utf8mb4 COLLATE utf8mb4_bin ";

    @Id
    @Column(nullable = false, length = 32)
    private String id;

    @Column(name = "created_time", columnDefinition = "datetime not null COMMENT '实体的创建时间'")
    private Date createdTime;

    public EntityBase(){
        this.createdTime = new Date();
    }

    public void generateId(){
        this.id = UUID.randomUUID().toString().replace("-","");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
