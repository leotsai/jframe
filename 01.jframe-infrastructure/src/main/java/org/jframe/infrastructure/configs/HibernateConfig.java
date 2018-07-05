package org.jframe.infrastructure.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by Leo on 2017/10/20.
 */
@Component
@PropertySource("/WEB-INF/app.properties")
public class HibernateConfig {

    @Value("${hibernate.connection.driverClass}")
    private String driverClass;

    @Value("${hibernate.connection.url}")
    private String url;

    @Value("${hibernate.connection.username}")
    private String username;

    @Value("${hibernate.connection.password}")
    private String password;

    @Value("${hibernate.connection.poolSize}")
    private Integer poolSize;

    @Value("${hibernate.jdbc.fetchSize}")
    private Integer fetchSize;

    @Value("${hibernate.jdbc.batchSize}")
    private Integer batchSize;

    @Value("${hibernate.dialect}")
    private String dialect;

    @Value("${hibernate.currentSessionContextClass}")
    private String currentSessionContextClass;

    @Value("${hibernate.showSql}")
    private Boolean showSql;

    @Value("${hibernate.hbm2ddl.auto}")
    private String hbm2ddlAuto;

    public String getDriverClass() {
        return driverClass;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Integer getPoolSize() {
        return poolSize;
    }

    public Integer getFetchSize() {
        return fetchSize;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public String getDialect() {
        return dialect;
    }

    public String getCurrentSessionContextClass() {
        return currentSessionContextClass;
    }

    public Boolean getShowSql() {
        return showSql;
    }

    public String getHbm2ddlAuto() {
        return hbm2ddlAuto;
    }
}
