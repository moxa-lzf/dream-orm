package com.moxa.dream.module.frame.callback;

import com.moxa.dream.antlr.expr.QueryExpr;
import com.moxa.dream.antlr.factory.AntlrInvokerFactory;
import com.moxa.dream.antlr.read.ExprReader;
import com.moxa.dream.antlr.smt.Statement;
import com.moxa.dream.antlr.util.InvokerUtil;
import com.moxa.dream.module.config.Configuration;
import com.moxa.dream.module.mapper.MethodInfo;
import com.moxa.dream.module.table.ColumnInfo;
import com.moxa.dream.module.table.TableInfo;
import com.moxa.dream.module.table.factory.TableFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SelectByIdCallback {
    protected Map<Class, Statement> statementMap = new HashMap<>();

    public Statement call(MethodInfo methodInfo, Object arg) {
        Class<?> type = arg.getClass();
        Statement statement = statementMap.get(type);
        if (statement == null) {
            synchronized (statementMap) {
                statement = statementMap.get(type);
                if (statement == null) {
                    Class colType = methodInfo.getColType();
                    Configuration configuration = methodInfo.getConfiguration();
                    TableFactory tableFactory = configuration.getTableFactory();
                    TableInfo tableInfo = tableFactory.getTableInfo(colType);
                    String table = tableInfo.getTable();
                    ColumnInfo primary = tableInfo.getPrimary();
                    String columns = tableInfo.getColumnInfoList().stream()
                            .map(columnInfo -> table + "." + columnInfo.getColumn())
                            .collect(Collectors.joining(","));
                    String sql = "select " + columns + " from " + table + " where " + table + "." + primary.getColumn() + "="
                            + InvokerUtil.wrapperInvokerSQL(AntlrInvokerFactory.NAMESPACE, AntlrInvokerFactory.$, ",",  primary.getName());
                    statement = new QueryExpr(new ExprReader(sql)).expr();
                    statementMap.put(type, statement);
                }
            }
        }
        return statement;
    }
}
