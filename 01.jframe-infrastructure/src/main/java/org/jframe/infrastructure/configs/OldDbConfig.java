package org.jframe.infrastructure.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by Leo on 2017/12/27.
 * 老系统数据库连接信息
 */
@Component
@PropertySource("/WEB-INF/app.properties")
public class OldDbConfig {
    @Value("${old.hibernate.url}")
    private String url;

    @Value("${old.hibernate.username}")
    private String username;

    @Value("${old.hibernate.password}")
    private String password;

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
