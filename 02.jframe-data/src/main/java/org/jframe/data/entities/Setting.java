package org.jframe.data.entities;

import org.jframe.data.core.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by leo on 2017-08-31.
 */
@Entity
@Table(name = "s_settings")
public class Setting extends EntityBase implements Serializable{
    private static final long serialVersionUID = -7075049863843294054L;
    @Column(name = "`key`", columnDefinition = "varchar(255) not null COMMENT 'KEY唯一标识'")
    private String key;

    @Column(name = "value", columnDefinition = "varchar(8000) null COMMENT '值'")
    private String value;

    @Column(name = "note", columnDefinition = "varchar(255) null COMMENT '备注，以代码为准'")
    private String note;

    public Setting(String key, String value, String note) {
        this.key = key;
        this.value = value;
        this.note = note;
    }

    public Setting() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
