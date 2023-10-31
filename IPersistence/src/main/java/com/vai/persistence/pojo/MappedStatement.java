package com.vai.persistence.pojo;

/**
 * @author yk
 * @since 2023-10-31
 */
public class MappedStatement {

    // id 标识
    private String id;
    // 参数值类型
    private String parameterType;
    // 返回值类型
    private String resultType;
    // sql语句
    private String sql;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
