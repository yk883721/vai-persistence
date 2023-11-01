package com.vai.test;

import com.vai.persistence.config.XmlConfigBuilder;
import com.vai.persistence.io.Resources;
import com.vai.persistence.pojo.Configuration;

import java.io.InputStream;

/**
 * @author yangk
 * @since 2023/11/1
 */
public class IPersistenceTest {

    public static void main(String[] args) throws Exception {

        InputStream inputStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        Configuration configuration = new XmlConfigBuilder().parseConfig(inputStream);

        System.out.println(configuration);

    }

}
