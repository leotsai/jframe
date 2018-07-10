package org.jframe.infrastructure.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Properties;

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
    private String poolSize;

    @Value("${hibernate.jdbc.fetchSize}")
    private String fetchSize;

    @Value("${hibernate.jdbc.batchSize}")
    private String batchSize;

    @Value("${hibernate.dialect}")
    private String dialect;

    @Value("${hibernate.currentSessionContextClass}")
    private String currentSessionContextClass;

    @Value("${hibernate.showSql}")
    private String showSql;

    @Value("${hibernate.hbm2ddl.auto}")
    private String hbm2ddlAuto;

    public Properties toProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.connection.driver_class", this.driverClass);
        properties.setProperty("hibernate.connection.url", this.url);
        properties.setProperty("hibernate.connection.username", this.username);
        properties.setProperty("hibernate.connection.password", this.password);
        properties.setProperty("hibernate.connection.pool_size", this.poolSize);
        properties.setProperty("hibernate.jdbc.fetch_size", this.fetchSize);
        properties.setProperty("hibernate.batch_size", this.batchSize);
        properties.setProperty("hibernate.dialect", this.dialect);
        properties.setProperty("hibernate.current_session_context_class", this.currentSessionContextClass);
        properties.setProperty("hibernate.show_sql", this.showSql);
        properties.setProperty("hibernate.hbm2ddl.auto", this.hbm2ddlAuto);
        return properties;
    }

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

    public String getPoolSize() {
        return poolSize;
    }

    public String getFetchSize() {
        return fetchSize;
    }

    public String getBatchSize() {
        return batchSize;
    }

    public String getDialect() {
        return dialect;
    }

    public String getCurrentSessionContextClass() {
        return currentSessionContextClass;
    }

    public String getShowSql() {
        return showSql;
    }

    public String getHbm2ddlAuto() {
        return hbm2ddlAuto;
    }
}
