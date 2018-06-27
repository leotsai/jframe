package org.jframe.data.core;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by leo on 2017-05-10.
 */
@MappedSuperclass
public class EntityBase {

    protected static final String EmojCharset = " CHARACTER SET utf8mb4 COLLATE utf8mb4_bin ";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_time", columnDefinition = "datetime not null COMMENT '实体的创建时间'")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    public boolean isToCreate() {
        return this.id == null || this.id == 0L;
    }

    public EntityBase() {
        this.createdTime = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
