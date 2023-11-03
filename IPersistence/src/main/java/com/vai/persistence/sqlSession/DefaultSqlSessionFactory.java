package com.vai.persistence.sqlSession;

import com.vai.persistence.pojo.Configuration;

/**
 * @author yangk
 * @since 2023/11/2
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    public SqlSession openSession(){
        return new DefaultSqlSession(configuration);
    }

}
