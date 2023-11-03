package com.vai.persistence.sqlSession;

import com.vai.persistence.pojo.Configuration;
import com.vai.persistence.pojo.MappedStatement;

import java.util.List;

/**
 * @author yangk
 * @since 2023/11/2
 */
public class DefaultSqlSession implements SqlSession{

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T> List<T> selectList(String statementId, Object... params) throws Exception {

        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);

        List<Object> list = simpleExecutor.query(configuration, mappedStatement, params);
        return (List<T>) list;
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) throws Exception {
        List<Object> objects = selectList(statementId, params);
        if(objects.size()==1){
            return (T) objects.get(0);
        }else {
            throw new RuntimeException("查询结果为空或者返回结果过多");
        }
    }

}
