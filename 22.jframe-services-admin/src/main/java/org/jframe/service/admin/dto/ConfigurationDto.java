package org.jframe.service.admin.dto;

import org.jframe.core.extensions.JList;
import org.jframe.core.helpers.StringHelper;
import org.jframe.data.entities.Setting;
import org.jframe.data.enums.SettingType;

/**
 * @author qq
 * @date 2018/7/12
 */
public class ConfigurationDto {
    private String key;
    private String value;
    private String note;
    private SettingType type;
    private String csvSource;
    private JList<String> sources;

    public ConfigurationDto() {

    }

    public ConfigurationDto(Setting setting) {
        this.key = setting.getKey();
        this.value = setting.getValue();
        this.note = setting.getNote();
        this.type = setting.getType();
        this.csvSource = setting.getCsvSource();
        this.sources = JList.splitByComma(this.csvSource).where(x -> !StringHelper.isNullOrEmpty(x));
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

    public JList<String> getSources() {
        return sources;
    }

    public void setSources(JList<String> sources) {
        this.sources = sources;
    }

    public String getCsvSource() {
        return csvSource;
    }

    public void setCsvSource(String csvSource) {
        this.csvSource = csvSource;
    }
}
