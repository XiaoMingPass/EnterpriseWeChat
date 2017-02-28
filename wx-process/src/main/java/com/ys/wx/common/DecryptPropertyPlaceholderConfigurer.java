package com.ys.wx.common;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

import java.io.*;
import java.util.Properties;

/**
 * Title : 加载资源文件
 * Description :
 * Author : Bruce.liu    date : 2017/1/12
 * Update :             date :
 * Version : 1.0.0
 * Copyright (c) 二龙湖基地组织  2016 ~ 2017 版权所有
 */
public class DecryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    private Resource[] locations;
    private String     fileEncoding;
    /* Linux的路径 */
    private String     linuxSystemPath;
    /* windows的路径 */
    private String     windowsSystemPath;

    private Properties props;

    public void setFileEncoding(String fileEncoding) {
        this.fileEncoding = fileEncoding;
    }

    public void setLocations(Resource[] locations) {
        this.locations = locations;
    }

    public void setLinuxSystemPath(String linuxSystemPath) {
        this.linuxSystemPath = linuxSystemPath;
    }

    public void setWindowsSystemPath(String windowsSystemPath) {
        this.windowsSystemPath = windowsSystemPath;
    }

    public void loadProperties(Properties props) throws IOException {
        if (this.locations != null) {
            PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
            for (Resource location1 : this.locations) {
                InputStream is;
                File configDir = new File(linuxSystemPath);
                if (!configDir.exists()) {
                    is = this.getClass().getResourceAsStream(
                            windowsSystemPath + "/"
                                    + location1.getFilename());
                } else {
                    String filePath = linuxSystemPath + File.separator
                            + location1.getFilename();
                    is = new FileInputStream(filePath);
                }
                try {
                    if (fileEncoding != null) {
                        propertiesPersister.load(props, new InputStreamReader(
                                is, fileEncoding));
                    } else {
                        propertiesPersister.load(props, is);
                    }
                    this.props = props;
                } finally {
                    if (is != null)
                        is.close();
                }
            }
        }
    }

    public String getValue(String key) {
        return props.getProperty(key);
    }
}
