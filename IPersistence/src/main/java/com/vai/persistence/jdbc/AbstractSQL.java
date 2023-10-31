package com.vai.persistence.jdbc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author yangk
 * @since 2023/10/23
 */
public abstract class AbstractSQL<T> {

    private static final String AND = ") \nAND (";
    private static final String OR = ") \nOR (";

    private SQLStatement sql = new SQLStatement();

    public abstract T getSelf();

    public T UPDATE(String table){
        sql().statementType = SQLStatement.StatementType.UPDATE;
        sql().tables.add(table);
        return getSelf();
    }

    public T SET(String sets){
        sql().sets.add(sets);
        return getSelf();
    }

    public T SET(String ...sets){
        sql().sets.addAll(Arrays.asList(sets));
        return getSelf();
    }

    public T INSERT_INTO(String tableName) {
        sql().statementType = SQLStatement.StatementType.INSERT;
        sql().tables.add(tableName);
        return getSelf();
    }

    public T VALUES(String columns, String values){
        INTO_COLUMNS(columns);
        INTO_VALUES(values);
        return getSelf();
    }

    public T INTO_COLUMNS(String ...columns){
        sql().columns.addAll(Arrays.asList(columns));
        return getSelf();
    }

    public T INTO_VALUES(String ...values){
        List<String> list = sql().valuesList.get(sql().valuesList.size() - 1);
        Collections.addAll(list, values);
        return getSelf();
    }

    public T SELECT(String columns){
        sql().statementType = SQLStatement.StatementType.SELECT;
        sql().select.add(columns);
        return getSelf();
    }

    public T SELECT(String ...columns){
        sql().statementType = SQLStatement.StatementType.SELECT;
        sql().select.addAll(Arrays.asList(columns));
        return getSelf();
    }

    public T SELECT_DISTINCT(String columns){
        sql().distinct = true;
        SELECT(columns);
        return getSelf();
    }

    public T SELECT_DISTINCT(String ...columns){
        sql().distinct = true;
        SELECT(columns);
        return getSelf();
    }

    public T DELETE_FROM(String table){
        sql().statementType = SQLStatement.StatementType.DELETE;
        sql().tables.add(table);
        return getSelf();
    }

    public T FROM(String table) {
        sql().tables.add(table);
        return getSelf();
    }

    public T FROM(String ...table) {
        sql().tables.addAll(Arrays.asList(table));
        return getSelf();
    }

    public T JOIN(String join){
        sql().join.add(join);
        return getSelf();
    }

    public T JOIN(String ...joins){
        sql().join.addAll(Arrays.asList(joins));
        return getSelf();
    }

    public T INNER_JOIN(String join){
        sql().innerJoin.add(join);
        return getSelf();
    }

    public T INNER_JOIN(String ...joins){
        sql().innerJoin.addAll(Arrays.asList(joins));
        return getSelf();
    }

    public T LEFT_OUTER_JOIN(String join){
        sql().leftJoin.add(join);
        return getSelf();
    }

    public T LEFT_OUTER_JOIN(String ...joins){
        sql().leftJoin.addAll(Arrays.asList(joins));
        return getSelf();
    }

    public T RIGHT_OUTER_JOIN(String join){
        sql().rightJoin.add(join);
        return getSelf();
    }

    public T RIGHT_OUTER_JOIN(String ...joins){
        sql().rightJoin.addAll(Arrays.asList(joins));
        return getSelf();
    }

    public T OUTER_JOIN(String join){
        sql().outerJoin.add(join);
        return getSelf();
    }

    public T OUTER_JOIN(String ...joins){
        sql().outerJoin.addAll(Arrays.asList(joins));
        return getSelf();
    }

    public T WHERE(String conditions){
        sql().where.add(conditions);
        sql().lastList = sql().where;
        return getSelf();
    }

    public T WHERE(String ...conditions){
        sql().where.addAll(Arrays.asList(conditions));
        sql().lastList = sql().where;
        return getSelf();
    }

    public T OR(){
        sql().lastList.add(OR);
        return getSelf();
    }

    public T AND(){
        sql().lastList.add(AND);
        return getSelf();
    }

    public T GROUP_BY(String columns){
        sql().groupBy.add(columns);
        return getSelf();
    }

    public T GROUP_BY(String ...columns){
        sql().groupBy.addAll(Arrays.asList(columns));
        return getSelf();
    }

    public T HAVING(String conditions){
        sql().having.add(conditions);
        sql().lastList = sql().having;
        return getSelf();
    }

    public T HAVING(String ...conditions){
        sql().having.addAll(Arrays.asList(conditions));
        sql().lastList = sql().having;
        return getSelf();
    }

    public T ORDER_BY(String columns){
        sql().orderBy.add(columns);
        return getSelf();
    }

    public T ORDER_BY(String ...columns){
        sql().orderBy.addAll(Arrays.asList(columns));
        return getSelf();
    }

    public T LIMIT(String variable){
        sql().limit = variable;
        sql().limitingRowStrategy = SQLStatement.LimitingRowStrategy.OFFSET_LIMIT;
        return getSelf();
    }

    public T LIMIT(int value){
        return LIMIT(String.valueOf(value));
    }

    public T OFFSET(String variable){
        sql().offset = variable;
        sql().limitingRowStrategy = SQLStatement.LimitingRowStrategy.OFFSET_LIMIT;
        return getSelf();
    }

    public T OFFSET(long value){
        return OFFSET(String.valueOf(value));
    }

    public T FETCH_FIRST_ROWS_ONLY(String variable){
        sql().limit = variable;
        sql().limitingRowStrategy = SQLStatement.LimitingRowStrategy.ISO;
        return getSelf();
    }

    public T FETCH_FIRST_ROWS_ONLY(int value){
        return FETCH_FIRST_ROWS_ONLY(String.valueOf(value));
    }

    public T OFFSET_ROWS(String variable){
        sql().offset = variable;
        sql().limitingRowStrategy = SQLStatement.LimitingRowStrategy.ISO;
        return getSelf();
    }

    public T OFFSET_ROWS(long value){
        return OFFSET_ROWS(String.valueOf(value));
    }

    public T ADD_ROW(){
        sql().valuesList.add(new ArrayList<>());
        return getSelf();
    }

    private SQLStatement sql(){
        return sql;
    }

    public <A extends Appendable> A usingAppender(A a){
        sql().sql(a);
        return a;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sql().sql(sb);
        return sb.toString();
    }

    private static class SafeAppendable{
        private final Appendable appendable;
        private boolean empty = true;

        public SafeAppendable(Appendable appendable) {
            this.appendable = appendable;
        }

        public SafeAppendable append(CharSequence s){
            try {
                if (empty && s.length() > 0) {
                    empty = false;
                }
                appendable.append(s);
            }catch (IOException e){
                throw new RuntimeException();
            }
            return this;
        }

        public boolean isEmpty() {
            return empty;
        }

    }

    private static class SQLStatement{

        public enum StatementType{
            INSERT, DELETE, UPDATE, SELECT;
        }

        public enum LimitingRowStrategy{
            NOP{
                @Override
                protected void appendClause(SafeAppendable builder, String offset, String limit) {

                }
            },
            ISO{
                @Override
                protected void appendClause(SafeAppendable builder, String offset, String limit) {
                    if (offset != null) {
                        builder.append(" OFFSET ").append(offset).append(" ROWS");
                    }
                    if (limit != null) {
                        builder.append(" FETCH FIRST ").append(limit).append(" ROWS ONLY");
                    }
                }
            },
            OFFSET_LIMIT{
                @Override
                protected void appendClause(SafeAppendable builder, String offset, String limit) {
                    if (limit != null) {
                        builder.append(" LIMIT ").append(limit);
                    }
                    if (offset != null) {
                        builder.append(" OFFSET ").append(offset);
                    }
                }
            };

            protected abstract void appendClause(SafeAppendable builder, String offset, String limit);

        }

        StatementType statementType;

        List<String> select = new ArrayList<>();
        List<String> columns = new ArrayList<>();
        List<String> tables = new ArrayList<>();
        List<String> where = new ArrayList<>();
        List<String> join = new ArrayList<>();
        List<String> innerJoin = new ArrayList<>();
        List<String> outerJoin = new ArrayList<>();
        List<String> leftJoin = new ArrayList<>();
        List<String> rightJoin = new ArrayList<>();
        List<String> sets = new ArrayList<>();
        List<String> orderBy = new ArrayList<>();
        List<String> lastList = new ArrayList<>();
        List<String> groupBy = new ArrayList<>();
        List<String> having = new ArrayList<>();

        List<List<String>> valuesList = new ArrayList<>();

        boolean distinct;
        String limit;
        String offset;

        LimitingRowStrategy limitingRowStrategy = LimitingRowStrategy.NOP;

        public SQLStatement(){
            // Prevent Synthetic Access
            this.valuesList.add(new ArrayList<>());
        }

        private static void sqlClause(SafeAppendable builder, String keyword, List<String> parts, String open, String close, String conjunction) {
            if (!parts.isEmpty()) {
                if (!builder.isEmpty()) {
                    builder.append("\n");
                }

                builder.append(keyword);
                builder.append(" ");
                builder.append(open);
                String last = "________";
                for (int i = 0; i < parts.size(); i++) {
                    String part = parts.get(i);
                    if (i > 0 && !part.equals(AND) && !part.equals(OR) && !last.equals(AND) && !last.equals(OR)) {
                        builder.append(conjunction);
                    }
                    builder.append(part);
                    last = part;
                }
                builder.append(close);
            }
        }

        private String selectSQL(SafeAppendable builder) {
            if (distinct) {
                sqlClause(builder, "SELECT DISTINCT", select, "", "", ", ");
            }else {
                sqlClause(builder, "SELECT", select, "", "", ", ");
            }

            sqlClause(builder, "FROM", tables, "",  "", ", ");
            joins(builder);
            sqlClause(builder, "WHERE", where, "(", ")", " AND ");
            sqlClause(builder, "GROUP BY", groupBy, "", "", ", ");
            sqlClause(builder, "HAVING", having, "(", ")", " AND ");
            sqlClause(builder, "ORDER BY", orderBy, "", "", ", ");
            limitingRowStrategy.appendClause(builder, offset, limit);
            return builder.toString();
        }

        private void joins(SafeAppendable builder){
            sqlClause(builder, "JOIN",  join, "", "", "\nJOIN ");
            sqlClause(builder, "INNER JOIN",  innerJoin, "", "", "\nINNER JOIN ");
            sqlClause(builder, "OUTER JOIN",  outerJoin, "", "", "\nOUTER JOIN ");
            sqlClause(builder, "LEFT OUTER JOIN",  leftJoin, "", "", "\nLEFT OUTER JOIN ");
            sqlClause(builder, "RIGHT OUTER JOIN",  rightJoin, "", "", "\nRIGHT OUTER JOIN ");
        }

        private String insertSQL(SafeAppendable builder) {
            sqlClause(builder, "INSERT INTO", tables, "", "", "");
            sqlClause(builder, "", columns, "(", ")", ", ");
            for (int i = 0; i < valuesList.size(); i++) {
                sqlClause(builder, i > 0 ? "," : "VALUES", valuesList.get(i), "(", ")", ", ");
            }
            return builder.toString();
        }

        public String deleteSQL(SafeAppendable builder) {
            sqlClause(builder, "DELETE FROM", tables, "", "", "");
            sqlClause(builder, "WHERE", where, "(", ")", " AND ");
            limitingRowStrategy.appendClause(builder, null, limit);
            return builder.toString();
        }

        public String updateSQL(SafeAppendable builder){
            sqlClause(builder, "UPDATE", tables, "", "", "");
            joins(builder);
            sqlClause(builder, "SET", sets, "", "", ", ");
            sqlClause(builder, "WHERE", where, "(", ")", " AND ");
            limitingRowStrategy.appendClause(builder, null, limit);
            return builder.toString();
        }

        public String sql(Appendable a){
            SafeAppendable builder = new SafeAppendable(a);
            if (statementType == null) {
                return null;
            }

            String answer;
            switch (statementType) {
                case DELETE:
                    answer = deleteSQL(builder);
                    break;
                case INSERT:
                    answer = insertSQL(builder);
                    break;
                case SELECT:
                    answer = selectSQL(builder);
                    break;
                case UPDATE:
                    answer = updateSQL(builder);
                    break;
                default:
                    answer = null;
            }
            return answer;
        }

    }

}
