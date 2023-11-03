package com.vai.persistence.sqlSession;

import com.vai.persistence.config.BoundSql;
import com.vai.persistence.pojo.Configuration;
import com.vai.persistence.pojo.MappedStatement;
import com.vai.persistence.utils.GenericTokenParser;
import com.vai.persistence.utils.ParameterMapping;
import com.vai.persistence.utils.ParameterMappingTokenHandler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yangk
 * @since 2023/11/2
 */
public class SimpleExecutor implements Executor{

    @Override
    public <T> List<T> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {
        // 1. 获取连接
        Connection connection = configuration.getDataSource().getConnection();

        // 2. sql语句
        String sql = mappedStatement.getSql();
        BoundSql boundSql = getBoundSql(sql);

        // 3. 预处理对象
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // 4. 设置参数
        String parameterType = mappedStatement.getParameterType();
        Class<?> parameterClass = getClassType(parameterType);

        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String content = parameterMapping.getContent();

            // 反射
            Field declaredField = parameterClass.getDeclaredField(content);
            declaredField.setAccessible(true);
            Object o = declaredField.get(params[0]);

            preparedStatement.setObject(i + 1, o);
        }

        // 5. 执行sql
        ResultSet resultSet = preparedStatement.executeQuery();
        String resultType = mappedStatement.getResultType();
        Class<?> resultClass = getClassType(resultType);

        List<Object> objects = new ArrayList<>();
        while (resultSet.next()) {
            Object o = resultClass.newInstance();

            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                String columnName = metaData.getColumnName(i);
                Object value = resultSet.getObject(i+1);

                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o, value);
            }
            objects.add(o);
        }

        return (List<T>) objects;
    }

    private Class<?> getClassType(String paramType) throws ClassNotFoundException {
        if (paramType != null) {
            return  Class.forName(paramType);
        }
        return null;
    }

    private BoundSql getBoundSql(String sql){
        ParameterMappingTokenHandler tokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser tokenParser = new GenericTokenParser("#{", "}", tokenHandler);

        String parsedSql = tokenParser.parse(sql);
        List<ParameterMapping> parameterMappings = tokenHandler.getParameterMappings();

        return new BoundSql(parsedSql, parameterMappings);
    }


}
