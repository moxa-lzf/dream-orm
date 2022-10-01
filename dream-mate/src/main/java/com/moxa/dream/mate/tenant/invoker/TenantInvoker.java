package com.moxa.dream.mate.tenant.invoker;

import com.moxa.dream.antlr.config.Assist;
import com.moxa.dream.antlr.exception.InvokerException;
import com.moxa.dream.antlr.handler.Handler;
import com.moxa.dream.antlr.invoker.AbstractInvoker;
import com.moxa.dream.antlr.invoker.Invoker;
import com.moxa.dream.antlr.smt.InvokerStatement;
import com.moxa.dream.antlr.sql.ToSQL;
import com.moxa.dream.mate.tenant.handler.TenantDeleteHandler;
import com.moxa.dream.mate.tenant.handler.TenantInsertHandler;
import com.moxa.dream.mate.tenant.handler.TenantQueryHandler;
import com.moxa.dream.mate.tenant.handler.TenantUpdateHandler;
import com.moxa.dream.mate.tenant.interceptor.TenantHandler;
import com.moxa.dream.mate.tenant.interceptor.TenantInterceptor;
import com.moxa.dream.system.config.Configuration;
import com.moxa.dream.system.mapped.MethodInfo;
import com.moxa.dream.system.plugin.factory.PluginFactory;
import com.moxa.dream.system.table.TableInfo;
import com.moxa.dream.system.table.factory.TableFactory;

import java.util.List;

public class TenantInvoker extends AbstractInvoker {
    private TableFactory tableFactory;
    private MethodInfo methodInfo;
    private TenantHandler tenantHandler;

    @Override
    public void init(Assist assist) {
        methodInfo = assist.getCustom(MethodInfo.class);
        Configuration configuration = methodInfo.getConfiguration();
        tableFactory = configuration.getTableFactory();
        PluginFactory pluginFactory = configuration.getPluginFactory();
        TenantInterceptor tenantInterceptor = pluginFactory.getInterceptor(TenantInterceptor.class);
        tenantHandler = tenantInterceptor.getTenantHandler();

    }

    @Override
    protected String invoker(InvokerStatement invokerStatement, Assist assist, ToSQL toSQL, List<Invoker> invokerList) throws InvokerException {
        String sql = toSQL.toStr(invokerStatement.getParamStatement(), assist, invokerList);
        invokerStatement.replaceWith(invokerStatement.getParamStatement());
        return sql;
    }

    @Override
    protected Handler[] handler() {
        return new Handler[]{new TenantQueryHandler(this), new TenantInsertHandler(this), new TenantUpdateHandler(this), new TenantDeleteHandler(this)};
    }


    public boolean isTenant(String table) {
        TableInfo tableInfo = tableFactory.getTableInfo(table);
        if (tableInfo != null) {
            return tenantHandler.isTenant(methodInfo, tableInfo);
        }
        return false;
    }

    public String getTenantColumn() {
        return tenantHandler.getTenantColumn();
    }
}