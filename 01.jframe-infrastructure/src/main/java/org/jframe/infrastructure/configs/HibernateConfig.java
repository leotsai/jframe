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

    @Value("${connection.provider_class}")
    private String providerClass;

    @Value("${hibernate.c3p0.max_size}")
    private String maxSize;

    @Value("${hibernate.c3p0.min_size}")
    private String minSize;

    @Value("${hibernate.c3p0.timeout}")
    private String timeout;

    @Value("${hibernate.c3p0.max_statements}")
    private String maxStatements;

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

        properties.setProperty("connection.provider_class", this.providerClass);
        properties.setProperty("hibernate.c3p0.max_size", this.maxSize);
        properties.setProperty("hibernate.c3p0.min_size", this.minSize);
        properties.setProperty("hibernate.c3p0.timeout", this.timeout);
        properties.setProperty("hibernate.c3p0.max_statements", this.maxStatements);

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

    public String getMaxStatements() {
        return maxStatements;
    }

    public String getTimeout() {
        return timeout;
    }

    public String getMinSize() {
        return minSize;
    }

    public String getMaxSize() {
        return maxSize;
    }

    public String getProviderClass() {
        return providerClass;
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
