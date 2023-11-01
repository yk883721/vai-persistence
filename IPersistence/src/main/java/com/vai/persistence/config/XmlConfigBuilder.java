package com.vai.persistence.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.vai.persistence.io.Resources;
import com.vai.persistence.pojo.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * @author yk
 * @since 2023-10-31
 */
public class XmlConfigBuilder {

    private Configuration configuration;

    public XmlConfigBuilder() {
        this.configuration = new Configuration();
    }

    /**
     * dom4j 解析主配置文件，封装 Configuration
     */
    public Configuration parseConfig(InputStream inputStream) throws DocumentException, PropertyVetoException {

        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();
        Element dataSourceElement = rootElement.element("dataSource");

        // 1. 数据源处理
        Properties datasourceProperties = new Properties();
        for (Iterator<Element> it = dataSourceElement.elementIterator("property"); it.hasNext();) {
            Element property = it.next();

            String propertyName = property.attributeValue("name");
            String propertyValue = property.attributeValue("value");

            datasourceProperties.put(propertyName, propertyValue);
        }

        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(datasourceProperties.getProperty("driverClass"));
        dataSource.setJdbcUrl(datasourceProperties.getProperty("jdbcUrl"));
        dataSource.setUser(datasourceProperties.getProperty("username"));
        dataSource.setPassword(datasourceProperties.getProperty("password"));
        configuration.setDataSource(dataSource);

        // 2. mapper 处理
        for (Iterator<Element> it = rootElement.elementIterator("mapper"); it.hasNext();) {
            Element mapperElement = it.next();
            String mapperResource = mapperElement.attributeValue("resource");

            InputStream mapperStream = Resources.getResourceAsStream(mapperResource);
            new XmlMapperBuilder(configuration).parse(mapperStream);
        }


        return configuration;
    }


}
