package com.moxa.dream.template.mapper;

import com.moxa.dream.antlr.factory.AntlrInvokerFactory;
import com.moxa.dream.antlr.util.InvokerUtil;
import com.moxa.dream.system.antlr.factory.SystemInvokerFactory;
import com.moxa.dream.system.config.Configuration;
import com.moxa.dream.system.config.MethodInfo;
import com.moxa.dream.system.core.session.Session;
import com.moxa.dream.system.table.ColumnInfo;
import com.moxa.dream.system.table.TableInfo;
import com.moxa.dream.system.table.factory.TableFactory;
import com.moxa.dream.template.condition.Condition;
import com.moxa.dream.template.util.ConditionObject;
import com.moxa.dream.template.util.SortObject;
import com.moxa.dream.template.util.TemplateUtil;
import com.moxa.dream.util.common.ObjectMap;
import com.moxa.dream.util.common.ObjectUtil;
import com.moxa.dream.util.exception.DreamRunTimeException;

import java.util.*;
import java.util.stream.Collectors;

public class SelectListSqlMapper implements SqlMapper {
    protected Session session;
    private Map<String, MethodInfo> methodInfoMap = new HashMap<>();

    public SelectListSqlMapper(Session session) {
        this.session = session;
    }

    @Override
    public Object execute(Class<?> type, Object... args) {
        return execute(type, args[0]);
    }

    protected Object execute(Class<?> type, Object arg) {
        String paramTypeName = null;
        if (arg != null) {
            paramTypeName = arg.getClass().getName();
        }
        String keyName = type.getName() + ":" + paramTypeName;
        MethodInfo methodInfo = methodInfoMap.get(keyName);
        if (methodInfo == null) {
            synchronized (this) {
                methodInfo = methodInfoMap.get(keyName);
                if (methodInfo == null) {
                    Configuration configuration = this.session.getConfiguration();
                    TableFactory tableFactory = configuration.getTableFactory();
                    methodInfo = getMethodInfo(configuration, tableFactory, type, arg);
                    methodInfo.compile();
                    methodInfoMap.put(keyName, methodInfo);
                }
            }
        }
        return session.execute(methodInfo, wrapArg(arg));
    }

    protected Map<String, Object> wrapArg(Object arg) {
        if (arg != null) {
            if (arg instanceof Map) {
                return (Map<String, Object>) arg;
            } else {
                return new ObjectMap(arg);
            }
        } else {
            return null;
        }
    }

    protected MethodInfo getMethodInfo(Configuration configuration, TableFactory tableFactory, Class type, Object arg) {
        Set<String> tableSet = TemplateUtil.getTableSet(type);
        String where = "";
        String orderBy = "";
        if (arg != null) {
            Class<?> argType = arg.getClass();
            where = getWhereSql(argType, tableSet, tableFactory);
            orderBy = getOrderSql(argType, tableSet, tableFactory);

        }
        String sql = "select " + InvokerUtil.wrapperInvokerSQL(
                SystemInvokerFactory.NAMESPACE,
                SystemInvokerFactory.ALL,
                ",")
                + "from " + InvokerUtil.wrapperInvokerSQL(
                SystemInvokerFactory.NAMESPACE,
                SystemInvokerFactory.TABLE,
                ",",
                tableSet.toArray(new String[0])) + where + orderBy;
        return new MethodInfo.Builder(configuration)
                .rowType(List.class)
                .colType(type)
                .sql(sql)
                .build();
    }

    protected String getTable(Class<?> type) {
        return TemplateUtil.getTable(type);
    }

    protected String getWhereSql(Class type, Set<String> tableSet, TableFactory tableFactory) {
        List<ConditionObject> conditionObjectList = TemplateUtil.getCondition(type);
        if (!ObjectUtil.isNull(conditionObjectList)) {
            Map<Boolean, List<ConditionObject>> booleanConditionObjectListMap = conditionObjectList.stream().collect(Collectors.groupingBy(conditionObject -> conditionObject.isFilterNull()));
            List<ConditionObject> conditionObjectFalseList = booleanConditionObjectListMap.get(false);
            List<ConditionObject> conditionObjectTrueList = booleanConditionObjectListMap.get(true);
            String whereFalseSql = getWhereSql(tableSet, tableFactory, conditionObjectFalseList);
            String whereTrueSql = getWhereSql(tableSet, tableFactory, conditionObjectTrueList);
            String whereSql = " where ";
            if (!ObjectUtil.isNull(whereFalseSql)) {
                whereSql = whereSql + whereFalseSql;
            }
            if (!ObjectUtil.isNull(whereTrueSql)) {
                whereTrueSql = InvokerUtil.wrapperInvokerSQL(AntlrInvokerFactory.NAMESPACE, AntlrInvokerFactory.NOT, ",", whereTrueSql);
                if (!ObjectUtil.isNull(whereFalseSql)) {
                    whereSql = whereSql + " and " + whereTrueSql;
                } else {
                    whereSql = whereSql + whereTrueSql;
                }
            }
            return whereSql;
        }
        return null;
    }

    protected String getWhereSql(Set<String> tableSet, TableFactory tableFactory, List<ConditionObject> conditionObjectList) {
        List<String> conditionList = new ArrayList<>();
        if (!ObjectUtil.isNull(conditionObjectList)) {
            for (ConditionObject conditionObject : conditionObjectList) {
                String table = conditionObject.getTable();
                String property = conditionObject.getProperty();
                Condition condition = conditionObject.getCondition();
                if (!ObjectUtil.isNull(table)) {
                    if (!tableSet.contains(table)) {
                        throw new DreamRunTimeException("条件表名限定" + tableSet);
                    }
                    TableInfo tableInfo = tableFactory.getTableInfo(table);
                    String fieldName = tableInfo.getFieldName(property);
                    ColumnInfo columnInfo = tableInfo.getColumnInfo(fieldName);
                    String conditionSql = condition.getCondition(tableInfo.getTable(), columnInfo.getColumn(), property);
                    conditionList.add(conditionSql);
                } else {
                    List<TableInfo> tableInfoList = tableSet.stream().map(tab -> {
                        TableInfo tableInfo = tableFactory.getTableInfo(tab);
                        return tableInfo;
                    }).filter(tableInfo -> {
                        String fieldName = tableInfo.getFieldName(conditionObject.getProperty());
                        if (fieldName == null) {
                            return false;
                        } else {
                            return true;
                        }
                    }).collect(Collectors.toList());
                    if (tableInfoList == null) {
                        throw new DreamRunTimeException("条件字段" + conditionObject.getProperty() + "在" + tableSet + "对应的类未注册");
                    }
                    if (tableInfoList.size() > 1) {
                        throw new DreamRunTimeException("条件字段" + conditionObject.getProperty() + "在" + tableInfoList.stream().map(tableInfo -> tableInfo.getTable()).collect(Collectors.toList()) + "对应的类都存在，请指定具体表名");
                    }
                    TableInfo tableInfo = tableInfoList.get(0);
                    String fieldName = tableInfo.getFieldName(property);
                    ColumnInfo columnInfo = tableInfo.getColumnInfo(fieldName);
                    String conditionSql = condition.getCondition(tableInfo.getTable(), columnInfo.getColumn(), property);
                    conditionList.add(conditionSql);
                }
            }
        }
        if (!ObjectUtil.isNull(conditionList)) {
            return String.join(" and ", conditionList);
        }
        return null;
    }

    protected String getOrderSql(Class type, Set<String> tableSet, TableFactory tableFactory) {
        List<SortObject> sortObjectList = TemplateUtil.getSort(type);
        List<String> orderList = new ArrayList<>();
        if (!ObjectUtil.isNull(sortObjectList)) {
            for (SortObject sortObject : sortObjectList) {
                String table = sortObject.getTable();
                if (!ObjectUtil.isNull(table)) {
                    if (!tableSet.contains(table)) {
                        throw new DreamRunTimeException("排序表名限定" + tableSet);
                    } else {
                        TableInfo tableInfo = tableFactory.getTableInfo(table);
                        String fieldName = tableInfo.getFieldName(sortObject.getProperty());
                        orderList.add(table + "." + tableInfo.getColumnInfo(fieldName).getColumn() + " " + sortObject.getOrderType());
                    }
                } else {
                    List<TableInfo> tableInfoList = tableSet.stream().map(tab -> {
                        TableInfo tableInfo = tableFactory.getTableInfo(tab);
                        return tableInfo;
                    }).filter(tableInfo -> {
                        String fieldName = tableInfo.getFieldName(sortObject.getProperty());
                        if (fieldName == null) {
                            return false;
                        } else {
                            return true;
                        }
                    }).collect(Collectors.toList());
                    if (tableInfoList == null) {
                        throw new DreamRunTimeException("排序字段" + sortObject.getProperty() + "在" + tableSet + "对应的类未注册");
                    }
                    if (tableInfoList.size() > 1) {
                        throw new DreamRunTimeException("排序字段" + sortObject.getProperty() + "在" + tableInfoList.stream().map(tableInfo -> tableInfo.getTable()).collect(Collectors.toList()) + "对应的类都存在，请指定具体表名");
                    }
                    TableInfo tableInfo = tableInfoList.get(0);
                    orderList.add(tableInfo.getColumnInfo(tableInfo.getFieldName(sortObject.getProperty())).getColumn() + " " + sortObject.getOrderType());
                }
            }
        }
        if (!ObjectUtil.isNull(orderList)) {
            return " order by " + String.join(",", orderList);
        } else {
            return "";
        }
    }
}
