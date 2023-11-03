package com.vai.persistence.sqlSession;

import java.util.List;

public interface SqlSession {

    <T> List<T> selectList(String statementId, Object... params) throws Exception;

    <T> T selectOne(String statementId, Object... params) throws Exception;



}
