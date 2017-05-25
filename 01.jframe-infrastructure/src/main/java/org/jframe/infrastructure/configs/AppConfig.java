package org.jframe.infrastructure.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by screw on 2017/5/22.
 */
@Component
@PropertySource("/WEB-INF/app.properties")
public class AppConfig {

    @Value("${app.folder}")
    private String rootFolder;

    @Value("${app.host}")
    private String host;

    @Value("${app.isTestServer}")
    private boolean isTestServer;

    @Value("${app.showFullError}")
    private boolean showFullError;

    @Value("${app.printLogs}")
    private boolean printLogs;

    @Value("${app.images.folder.original}")
    private String imageOriginalFolder;

    @Value("${app.images.folder.sizes}")
    private String imageSizesFolder;


    //--------------------------------

    public String getRootFolder() {
        return rootFolder;
    }

    public String getHost() {
        return host;
    }

    public boolean isShowFullError() {
        return showFullError;
    }

    public boolean isTestServer() {
        return isTestServer;
    }

    public boolean isPrintLogs() {
        return printLogs;
    }

    public String getImageOriginalFolder() {
        return imageOriginalFolder;
    }

    public String getImageSizesFolder() {
        return imageSizesFolder;
    }

}
