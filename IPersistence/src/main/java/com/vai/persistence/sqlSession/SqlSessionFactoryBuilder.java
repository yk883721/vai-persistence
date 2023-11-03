package com.vai.persistence.sqlSession;

import com.vai.persistence.config.XmlConfigBuilder;
import com.vai.persistence.pojo.Configuration;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;

/**
 * @author yangk
 * @since 2023/11/2
 */
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(InputStream inputStream) throws PropertyVetoException, DocumentException {
        Configuration configuration = new XmlConfigBuilder().parseConfig(inputStream);
        return new DefaultSqlSessionFactory(configuration);
    }

}
