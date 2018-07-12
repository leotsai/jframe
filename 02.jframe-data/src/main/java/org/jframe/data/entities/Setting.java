package org.jframe.data.entities;

import org.jframe.core.exception.KnownException;
import org.jframe.core.extensions.JList;
import org.jframe.core.helpers.JsonHelper;
import org.jframe.core.helpers.StringHelper;
import org.jframe.data.converters.SettingTypeConverter;
import org.jframe.data.core.EntityBase;
import org.jframe.data.enums.SettingType;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * Created by leo on 2017-08-31.
 */
@Entity
@Table(name = "s_settings")
public class Setting extends EntityBase implements Serializable {
    private static final long serialVersionUID = -7075049863843294054L;
    @Column(name = "`key`", columnDefinition = "varchar(255) not null COMMENT 'KEY唯一标识'")
    private String key;

    @Column(name = "value", columnDefinition = "varchar(8000) null COMMENT '值'")
    private String value;

    @Column(name = "note", columnDefinition = "varchar(255) null COMMENT '备注，以代码为准'")
    private String note;

    @Column(name = "`type`", columnDefinition = "int not null COMMENT '" + SettingType.Doc + "'")
    @Convert(converter = SettingTypeConverter.class)
    private SettingType type;

    @Column(name = "csv_source", columnDefinition = "varchar(8000) null COMMENT '数据源'")
    private String csvSource;

    public Setting() {
        this.type = SettingType.TEXT;
    }

    public Setting(String key, String value, String note) {
        this.key = key;
        this.value = value;
        this.note = note;
        this.type = SettingType.TEXT;
    }

    public Setting(String key, String value, String note, SettingType type, String csvSource) {
        this.key = key;
        this.value = value;
        this.note = note;
        this.type = type;
        this.csvSource = csvSource;
    }

    public void validateValue() {
        switch (this.type) {
            case JSON:
                Map map = JsonHelper.tryDeserialize(this.value, Map.class);
                if (map == null) {
                    throw new KnownException("请输入key-value形式的JSON");
                }
                break;
            case BOOLEAN:
                if (Objects.equals("true", this.value) || Objects.equals("false", this.value)) {
                    break;
                }
                throw new KnownException("boolean值不正确");
            case CHECKBOX:
                JList values = JsonHelper.tryDeserialize(this.value, JList.class);
                if (values == null) {
                    throw new KnownException("输入数据错误");
                }
                if (!JList.splitByComma(this.csvSource).containsAll(values)) {
                    throw new KnownException("值不合法");
                }
                break;
            case SELECT:
                if (!JList.splitByComma(this.csvSource).contains(this.value)) {
                    throw new KnownException("值不合法");
                }
                break;
            default:
                if (StringHelper.isNullOrEmpty(this.value)) {
                    throw new KnownException("值不能为空");
                }
                break;

        }
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

    public SettingType getType() {
        return type;
    }

    public void setType(SettingType type) {
        this.type = type;
    }

    public String getCsvSource() {
        return csvSource;
    }

    public void setCsvSource(String csvSource) {
        this.csvSource = csvSource;
    }
}
