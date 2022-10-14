package com.moxa.dream.system.core.action;


import com.moxa.dream.antlr.config.Command;
import com.moxa.dream.system.config.Configuration;
import com.moxa.dream.system.config.MethodInfo;
import com.moxa.dream.system.core.executor.Executor;
import com.moxa.dream.system.core.session.Session;
import com.moxa.dream.system.core.session.SessionFactory;
import com.moxa.dream.util.common.NonCollection;
import com.moxa.dream.util.common.ObjectMap;
import com.moxa.dream.util.common.ObjectUtil;
import com.moxa.dream.util.common.ObjectWrapper;
import com.moxa.dream.util.exception.DreamRunTimeException;
import com.moxa.dream.util.reflect.ReflectUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Properties;

public class SqlAction implements Action {
    private final Configuration configuration;
    private final String sql;
    private final String property;
    private MethodInfo methodInfo;
    private boolean cache = true;
    private Command command = Command.NONE;

    public SqlAction(Configuration configuration, String property, String sql) {
        this.configuration = configuration;
        this.property = property;
        this.sql = sql;
    }

    @Override
    public void setProperties(Properties properties) {
        String cache = properties.getProperty("cache");
        this.cache = !String.valueOf(false).equalsIgnoreCase(cache);
        String command = properties.getProperty("command");
        if (!ObjectUtil.isNull(command)) {
            command = command.toLowerCase();
            switch (command) {
                case "query":
                    this.command = Command.QUERY;
                    break;
                case "insert":
                    this.command = Command.INSERT;
                    break;
                case "update":
                    this.command = Command.UPDATE;
                    break;
                case "delete":
                    this.command = Command.DELETE;
                    break;
                default:
                    throw new DreamRunTimeException("Command:'" + command + "'未注册");

            }
        }
    }

    @Override
    public void doAction(Executor executor, Object arg) throws Exception {
        if (!ObjectUtil.isNull(property)) {
            if (methodInfo == null) {
                synchronized (this) {
                    if (methodInfo == null) {
                        int len;
                        Field field;
                        if ((len = property.lastIndexOf(".")) >= 0) {
                            Object target = ObjectWrapper.wrapper(arg).get(property.substring(0, len));
                            ObjectUtil.requireNonNull(target, "对象地址'" + property.substring(0, len) + "'为空");
                            field = target.getClass().getDeclaredField(property.substring(len + 1));
                        } else {
                            field = arg.getClass().getDeclaredField(property);
                        }
                        Type type = field.getGenericType();
                        methodInfo = new MethodInfo.Builder(configuration)
                                .rowType(ReflectUtil.getRowType(type))
                                .colType(ReflectUtil.getColType(type))
                                .cache(cache)
                                .command(command)
                                .sql(sql)
                                .build();
                        methodInfo.compile();
                    }
                }
            }
        } else {
            if (methodInfo == null) {
                synchronized (this) {
                    if (methodInfo == null) {
                        methodInfo = new MethodInfo.Builder(configuration)
                                .rowType(NonCollection.class)
                                .colType(Object.class)
                                .cache(cache)
                                .command(command)
                                .sql(sql)
                                .build();
                        methodInfo.compile();
                    }
                }
            }
        }
        SessionFactory sessionFactory = executor.getSessionFactory();
        Session session = sessionFactory.openSession(executor);
        Map<String, Object> argMap = null;
        if (arg != null) {
            if (arg instanceof Map) {
                argMap = (Map<String, Object>) arg;
            } else {
                argMap = new ObjectMap(arg);
            }
        }
        Object result = session.execute(methodInfo, argMap);
        if (!ObjectUtil.isNull(property)) {
            ObjectWrapper.wrapper(arg).set(property, result);
        }
    }
}