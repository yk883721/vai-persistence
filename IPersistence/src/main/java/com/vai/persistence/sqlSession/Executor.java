package com.vai.persistence.sqlSession;

import com.vai.persistence.pojo.Configuration;
import com.vai.persistence.pojo.MappedStatement;

import java.util.List;

public interface Executor {

    <T> List<T> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;

}
