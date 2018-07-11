package org.jframe.data.entities;

import org.jframe.core.extensions.JDate;
import org.jframe.core.logging.enums.LogArea;
import org.jframe.data.converters.LogTypeConverter;
import org.jframe.data.core.EntityBase;
import org.jframe.infrastructure.helpers.DateHelper;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * created by yezi on 2018/7/9
 */
@Entity
@Table(name = "s_mysql_logs")
public class LogMySql extends EntityBase {

    @Column(name = "log", columnDefinition = "longtext not null COMMENT '日志详情'")
    private String log;

    @Column(name = "`group`", columnDefinition = "varchar(250) not null COMMENT '日志分组'")
    private String group;

    @Column(name = "location", columnDefinition = "varchar(250) not null COMMENT '日志记录位置'")
    private String location;

    @Column(name = "type", columnDefinition = "int not null COMMENT '" + LogArea.Doc + "'")
    @Convert(converter = LogTypeConverter.class)
    private LogArea type;

    public LogMySql() {

    }

    public LogMySql(String group, String log, LogArea type, String location) {
        this.group = group;
        this.log = log;
        this.type = type;
        this.location = location;
    }

    public String formatFileLog() {
        return "\r\n" + DateHelper.toDate(JDate.now()) + this.type + "；" + this.group + "；" + this.location + "；" + this.log;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LogArea getType() {
        return type;
    }

    public void setType(LogArea type) {
        this.type = type;
    }
}
