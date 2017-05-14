package org.jframe.data.entities;

import org.jframe.data.core.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by leo on 2017-05-10.
 */
@Entity(name = "images")
public class Image extends EntityBase{

    @Column(name = "`key`", length = 100, nullable = false)
    private String key;

    @Column(name = "original_path", length = 255, nullable = false)
    private String originalPath;

    @Column(length = 100, nullable = true)
    private String name;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
