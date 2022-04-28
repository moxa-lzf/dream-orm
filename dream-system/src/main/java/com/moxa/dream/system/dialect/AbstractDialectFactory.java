package com.moxa.dream.system.dialect;

import com.moxa.dream.antlr.bind.ResultInfo;
import com.moxa.dream.antlr.exception.InvokerException;
import com.moxa.dream.antlr.expr.PackageExpr;
import com.moxa.dream.antlr.expr.SqlExpr;
import com.moxa.dream.antlr.factory.AntlrInvokerFactory;
import com.moxa.dream.antlr.factory.InvokerFactory;
import com.moxa.dream.antlr.factory.MyFunctionFactory;
import com.moxa.dream.antlr.invoker.$Invoker;
import com.moxa.dream.antlr.invoker.ScanInvoker;
import com.moxa.dream.antlr.read.ExprReader;
import com.moxa.dream.antlr.smt.PackageStatement;
import com.moxa.dream.antlr.sql.ToSQL;
import com.moxa.dream.system.antlr.decoration.AnnotationDecoration;
import com.moxa.dream.system.antlr.decoration.Decoration;
import com.moxa.dream.system.antlr.decoration.ScanDecoration;
import com.moxa.dream.system.antlr.factory.SystemInvokerFactory;
import com.moxa.dream.system.cache.CacheKey;
import com.moxa.dream.system.config.Configuration;
import com.moxa.dream.system.mapped.MappedParam;
import com.moxa.dream.system.mapped.MappedSql;
import com.moxa.dream.system.mapped.MappedStatement;
import com.moxa.dream.system.mapper.MethodInfo;
import com.moxa.dream.system.table.ColumnInfo;
import com.moxa.dream.system.table.TableInfo;
import com.moxa.dream.system.table.factory.TableFactory;
import com.moxa.dream.system.typehandler.factory.TypeHandlerFactory;
import com.moxa.dream.system.typehandler.handler.TypeHandler;
import com.moxa.dream.util.common.ObjectUtil;
import com.moxa.dream.util.common.ObjectWrapper;

import java.sql.Types;
import java.util.*;

public abstract class AbstractDialectFactory implements DialectFactory {
    private static final int SPLIT = 5;
    protected ToSQL toSQL;
    private int split;

    public AbstractDialectFactory() {
        this(SPLIT);
    }

    public AbstractDialectFactory(int split) {
        this.split = split;
        ObjectUtil.requireTrue(split > 0, "Property 'split' must be greater than 0");
    }

    @Override
    public void setDialect(ToSQL toSQL) {
        this.toSQL = toSQL;
    }

    @Override
    public PackageStatement compile(MethodInfo methodInfo) {
        String sql = methodInfo.getSql();
        ExprReader exprReader = new ExprReader(sql, getMyFunctionFactory());
        SqlExpr sqlExpr = new PackageExpr(exprReader);
        PackageStatement statement = (PackageStatement) sqlExpr.expr();
        return statement;
    }

    @Override
    public MappedStatement compile(MethodInfo methodInfo, Object arg) {
        List<MappedParam> mappedParamList = null;
        PackageStatement statement = methodInfo.getStatement();
        ScanInvoker.ScanInfo scanInfo = statement.getValue(ScanInvoker.ScanInfo.class);
        List<$Invoker.ParamInfo> paramInfoList = null;
        String sql = null;
        if (scanInfo != null) {
            paramInfoList = scanInfo.getParamInfoList();
            if (paramInfoList != null) {
                sql = scanInfo.getSql();
                if (paramInfoList.size() > 0) {
                    ObjectWrapper paramWrapper = ObjectWrapper.wrapper(arg);
                    for ($Invoker.ParamInfo paramInfo : paramInfoList) {
                        paramInfo.setParamValue(paramWrapper.get(paramInfo.getParamName()));
                    }
                }
            }
        }
        if (ObjectUtil.isNull(sql)) {
            ResultInfo resultInfo = getResultInfo(methodInfo, statement, arg);
            sql = resultInfo.getSql();
            if (scanInfo == null) {
                scanInfo = statement.getValue(ScanInvoker.ScanInfo.class);
            }
            $Invoker invoker = resultInfo.getSqlInvoker($Invoker.class);
            if (invoker != null) {
                paramInfoList = invoker.getParamInfoList();
            } else {
                paramInfoList = new ArrayList<>();
                scanInfo.setParamInfoList(paramInfoList);
            }
        }
        if (!ObjectUtil.isNull(paramInfoList)) {
            mappedParamList = new ArrayList<>();
            ParamTypeMap paramTypeMap = methodInfo.get(ParamTypeMap.class);
            if (paramTypeMap == null) {
                synchronized (this) {
                    paramTypeMap = methodInfo.get(ParamTypeMap.class);
                    if (paramTypeMap == null) {
                        paramTypeMap = new ParamTypeMap();
                        methodInfo.set(ParamTypeMap.class, paramTypeMap);
                    }
                }
            }
            Configuration configuration = methodInfo.getConfiguration();
            TableFactory tableFactory = configuration.getTableFactory();
            Map<String, ScanInvoker.ParamScanInfo> paramScanInfoMap = scanInfo.getParamScanInfoMap();
            for ($Invoker.ParamInfo paramInfo : paramInfoList) {
                ParamType paramType = paramTypeMap.get(paramInfo.getParamName());
                if (paramType == null) {
                    TypeHandlerFactory typeHandlerFactory = configuration.getTypeHandlerFactory();
                    ObjectUtil.requireNonNull(typeHandlerFactory, "Property 'typeHandlerFactory' is required");
                    ScanInvoker.ParamScanInfo paramScanInfo = paramScanInfoMap.get(paramInfo.getParamName());
                    Object value = paramInfo.getParamValue();
                    if (paramScanInfo != null) {
                        String table = paramScanInfo.getTable();
                        String column = paramScanInfo.getColumn();
                        TableInfo tableInfo = null;
                        Map<String, ScanInvoker.TableScanInfo> tableScanInfoMap = scanInfo.getTableScanInfoMap();
                        if (ObjectUtil.isNull(table)) {
                            Collection<ScanInvoker.TableScanInfo> tableScanInfoList = tableScanInfoMap.values();
                            ObjectUtil.requireNonNull(tableScanInfoList, "@Function '" + ScanInvoker.class.getName() + "' has no scan table");
                            for (ScanInvoker.TableScanInfo tableScanInfo : tableScanInfoList) {
                                tableInfo = tableFactory.getTableInfo(tableScanInfo.getTable());
                                if (tableInfo != null) {
                                    break;
                                }
                            }
                        } else {
                            ScanInvoker.TableScanInfo tableScanInfo = tableScanInfoMap.get(table);
                            if (tableScanInfo != null) {
                                table = tableScanInfo.getTable();
                            }
                            tableInfo = tableFactory.getTableInfo(table);
                        }
                        ObjectUtil.requireNonNull(tableInfo, "tableInfo was not found,table is '" + table + "',column is '" + column + "'");
                        String fieldName = tableInfo.getFieldName(column);
                        if (ObjectUtil.isNull(fieldName))
                            fieldName = column;
                        ColumnInfo columnInfo = tableInfo.getColumnInfo(fieldName);
                        int jdbcType = columnInfo.getJdbcType();
                        paramTypeMap.put(paramInfo.getParamName(),
                                paramType = new ParamType(columnInfo, typeHandlerFactory.getTypeHandler(value == null ? Object.class : value.getClass(), jdbcType)));
                    } else {
                        paramTypeMap.put(paramInfo.getParamName(),
                                paramType = new ParamType(null, typeHandlerFactory.getTypeHandler(value == null ? Object.class : value.getClass(), Types.NULL)));
                    }
                }
                mappedParamList.add(getMappedParam(paramType.getColumnInfo(), paramInfo.getParamValue(), paramType.getTypeHandler()));
            }
        }
        return new MappedStatement
                .Builder()
                .methodInfo(methodInfo)
                .mappedSql(new MappedSql(scanInfo.getCommand(), sql, scanInfo.getTableScanInfoMap()))
                .mappedParamList(mappedParamList)
                .arg(arg)
                .build();
    }

    protected ResultInfo getResultInfo(MethodInfo methodInfo, PackageStatement statement, Object arg) {
        Map<Class, Object> allCustomMap = new HashMap<>();
        Map<Class, Object> defaultCustomMap = getDefaultCustomMap(methodInfo, arg);
        Map<Class<?>, Object> customMap = getCustomMap(methodInfo, arg);
        allCustomMap.putAll(defaultCustomMap);
        if (!ObjectUtil.isNull(customMap)) {
            for (Class<?> key : customMap.keySet()) {
                allCustomMap.put(key, customMap.get(key));
            }
        }
        List<InvokerFactory> allInvokerFactoryList = new ArrayList<>();
        List<InvokerFactory> defaultInvokerFactoryList = getDefaultInvokerFactoryList();
        List<InvokerFactory> invokerFactoryList = getInvokerFactoryList();
        allInvokerFactoryList.addAll(defaultInvokerFactoryList);
        if (!ObjectUtil.isNull(invokerFactoryList)) {
            allInvokerFactoryList.addAll(invokerFactoryList);
        }
        try {
            ResultInfo resultInfo = toSQL.toResult(statement, allInvokerFactoryList, allCustomMap);
            return resultInfo;
        } catch (InvokerException e) {
            throw new DialectException(e);
        }
    }

    private List<InvokerFactory> getDefaultInvokerFactoryList() {
        List<InvokerFactory> invokerFactoryList = new ArrayList<>();
        invokerFactoryList.addAll(Arrays.asList(new AntlrInvokerFactory(), new SystemInvokerFactory()));
        return invokerFactoryList;
    }

    protected List<InvokerFactory> getInvokerFactoryList() {
        return null;
    }

    private Map<Class, Object> getDefaultCustomMap(MethodInfo methodInfo, Object arg) {
        Map<Class, Object> customMap = new HashMap<>();
        customMap.put(MethodInfo.class, methodInfo);
        if (arg == null) {
            arg = new HashMap<>();
        }
        customMap.put(ObjectWrapper.class, ObjectWrapper.wrapper(arg));
        return customMap;

    }

    protected abstract <T> Map<Class<? extends T>, T> getCustomMap(MethodInfo methodInfo, Object arg);

    protected MappedParam getMappedParam(ColumnInfo columnInfo, Object paramValue, TypeHandler typeHandler) {
        int jdbcType = Types.NULL;
        if (columnInfo != null) {
            jdbcType = columnInfo.getJdbcType();
        }
        return new MappedParam(jdbcType, paramValue, typeHandler);
    }

    @Override
    public CacheKey getCacheKey(MethodInfo methodInfo) {
        String sql = methodInfo.getSql();
        ObjectUtil.requireTrue(!ObjectUtil.isNull(sql), "Property 'sql' is required");
        char[] charList = sql.toCharArray();
        int index = 0;
        for (int i = 0; i < charList.length; i++) {
            char c;
            if (!Character.isWhitespace(c = charList[i])) {
                charList[index++] = Character.toLowerCase(c);
            }
        }
        if (split > index)
            split = index;
        Object[] hashObj = new Object[split + 2];
        int len = (int) Math.ceil(index / (double) split);
        for (int i = 0; i < split; i++) {
            int sPoint = i * len;
            int size = Math.min((i + 1) * len, index) - sPoint;
            char[] tempChars = new char[size];
            System.arraycopy(charList, sPoint, tempChars, 0, size);
            hashObj[i] = new String(tempChars);
        }
        hashObj[split] = methodInfo.getColType();
        hashObj[split + 1] = methodInfo.getRowType();
        CacheKey cacheKey = new CacheKey();
        cacheKey.update(hashObj);
        return cacheKey;
    }

    protected abstract MyFunctionFactory getMyFunctionFactory();

    @Override
    public void decoration(MethodInfo methodInfo) {
        List<Decoration> allDecorationList = new ArrayList<>();
        List<Decoration> beforeDecorationList = List.of(new AnnotationDecoration());
        List<Decoration> decorationList = getWrapList();
        List<Decoration> afterDecorationList = List.of(new ScanDecoration());
        allDecorationList.addAll(beforeDecorationList);
        if (!ObjectUtil.isNull(decorationList)) {
            allDecorationList.addAll(decorationList);
        }
        allDecorationList.addAll(afterDecorationList);
        for (Decoration decoration : allDecorationList) {
            decoration.decorate(methodInfo);
        }
    }

    protected abstract List<Decoration> getWrapList();


    static class ParamTypeMap {
        private Map<String, ParamType> paramTypeMap = new HashMap<>();

        public ParamType get(String param) {
            return paramTypeMap.get(param);
        }

        public void put(String param, ParamType paramType) {
            paramTypeMap.put(param, paramType);
        }
    }

    static class ParamType {
        private ColumnInfo columnInfo;
        private TypeHandler typeHandler;

        public ParamType(ColumnInfo columnInfo, TypeHandler typeHandler) {
            this.columnInfo = columnInfo;
            this.typeHandler = typeHandler;
        }

        public ColumnInfo getColumnInfo() {
            return columnInfo;
        }

        public TypeHandler getTypeHandler() {
            return typeHandler;
        }
    }


}