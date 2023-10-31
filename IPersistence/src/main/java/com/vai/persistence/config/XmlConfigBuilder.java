package com.vai.persistence.config;

import com.vai.persistence.pojo.Configuration;

import java.io.InputStream;

/**
 * @author yk
 * @since 2023-10-31
 */
public class XmlConfigBuilder {

    /**
     * dom4j 解析主配置文件，封装 Configuration
     * @param inputStream
     * @return
     */
    public Configuration parseConfig(InputStream inputStream){
        // todo
        return new Configuration();
    }

}
